package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.dto.TokenDto;
import com.miracle.miraclemorningback.Util.CookieUtil;
import com.miracle.miraclemorningback.dto.MemberRequestDto;
import com.miracle.miraclemorningback.dto.MemberResponseDto;
import com.miracle.miraclemorningback.dto.RequestSuccessDto;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.RefreshTokenEntity;
import com.miracle.miraclemorningback.entity.Role;
import com.miracle.miraclemorningback.jwt.JwtTokenProvider;
import com.miracle.miraclemorningback.repository.MemberRepository;
import com.miracle.miraclemorningback.repository.RefreshTokenRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

        private final MemberRepository memberRepository;

        private final RefreshTokenRepository refreshTokenRepository;

        private final JwtTokenProvider jwtTokenProvider;

        @Value("${su.name}")
        private String su;

        // 전체 회원 조회
        @Transactional(readOnly = true)
        public ResponseEntity<Object> getMembers() {
                List<MemberResponseDto> members = memberRepository.findAll().stream()
                                .map(memberEntity -> MemberResponseDto.builder()
                                                .memberId(memberEntity.getMemberId())
                                                .memberName(memberEntity.getMemberName())
                                                .password(memberEntity.getPassword())
                                                .role(memberEntity.getRole())
                                                .createdAt(memberEntity.getCreatedAt())
                                                .build())
                                .toList();

                return ResponseEntity.ok().body(members);
        }

        // 회원 등록
        @Transactional
        public ResponseEntity<Object> registerMember(MemberRequestDto requestDto) {

                String memberName = requestDto.getMemberName();

                // 중복된 memberName 확인
                if (memberRepository.existsByMemberName(memberName)) {
                        // 이미 존재하는 닉네임인 경우 예외 처리 및 409 Conflict 반환
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("이미 존재하는 닉네입니다.")
                                        .build();

                        return ResponseEntity.status(HttpStatus.CONFLICT).body(requestSuccessDto);
                }

                MemberEntity memberEntity = MemberEntity.builder()
                                .memberName(requestDto.getMemberName())
                                .password(requestDto.getPassword())
                                .role(Role.TEMP_USER)
                                .build();

                memberRepository.save(memberEntity);

                RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                .success(true)
                                .message("요청이 성공적으로 처리되었습니다.")
                                .build();

                return ResponseEntity.ok().body(requestSuccessDto);
        }

        // 회원 권한 수정
        @Transactional
        public ResponseEntity<Object> updateMemberRole(MemberRequestDto requestDto) {
                MemberEntity memberEntity = memberRepository.findById(requestDto.getMemberId()).orElseGet(null);

                if (memberEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                } else if (memberEntity.getMemberName().equals(su)) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("관리자는 권한이 변경될 수 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(requestSuccessDto);
                } else {
                        memberRepository.updateMemberRole(requestDto.getMemberId(), requestDto.getRole());

                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(true)
                                        .message("요청이 성공적으로 처리되었습니다.")
                                        .build();

                        return ResponseEntity.ok().body(requestSuccessDto);
                }
        }

        // 회원 삭제
        @Transactional
        public ResponseEntity<Object> deleteMember(Long memberId, String password, String memberName) {
                MemberEntity memberEntity = memberRepository.findById(memberId).orElse(null);
                if (memberEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                } else if (memberEntity.getMemberName().equals(su)) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("관리자는 삭제할 수 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(requestSuccessDto);
                } else {
                        if (memberName.equals(su) || password.equals(memberEntity.getPassword())) {
                                memberRepository.deleteById(memberId);
                                RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                                .success(true)
                                                .message("요청이 성공적으로 처리되었습니다.")
                                                .build();
                                return ResponseEntity.ok().body(requestSuccessDto);
                        } else {
                                RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                                .success(false)
                                                .message("해당하는 리소스가 없습니다.")
                                                .build();
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                        }
                }
        }

        // 로그인
        @Transactional
        public ResponseEntity<Object> loginMember(HttpServletResponse response, MemberRequestDto requestDto) {

                MemberEntity memberEntity = memberRepository.findByMemberName(requestDto.getMemberName()).orElse(null);

                // 닉네임 또는 비밀번호가 일치하지 않으면 UNAUTHORIZED 상태 코드를 반환
                if (memberEntity == null || !requestDto.getPassword().equals(memberEntity.getPassword())) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder().success(false)
                                        .message("인증에 실패했습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(requestSuccessDto);
                }

                // 승인된 사용자인 경우 토큰 생성 후 반환
                if (memberEntity.getRole() == Role.USER || memberEntity.getRole() == Role.ADMIN) {

                        String accessToken = jwtTokenProvider.generateAccessToken(
                                        memberEntity.getMemberId(),
                                        memberEntity.getMemberName(),
                                        memberEntity.getRole());

                        String refreshToken = jwtTokenProvider.generateRefreshToken();

                        TokenDto tokenDto = TokenDto.builder()
                                        .accessToken(accessToken)
                                        .build();

                        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                                        .memberId(memberEntity.getMemberId())
                                        .refreshToken(refreshToken)
                                        .accessToken(accessToken)
                                        .build();

                        refreshTokenRepository.save(refreshTokenEntity);

                        // refreshToken이 담긴 쿠키 생성
                        CookieUtil.generateRefreshTokenCookie(response, refreshToken);

                        // 헤더에 쿠키 정보 포함하여 응답
                        return ResponseEntity.ok().body(tokenDto);
                } else {
                        // 승인되지 않은 사용자인 경우 FORBIDDEN 상태 코드를 반환
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder().success(false)
                                        .message("승인되지 않은 사용자입니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(requestSuccessDto);
                }
        }
}