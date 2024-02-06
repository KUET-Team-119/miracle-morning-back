package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.dto.TokenDto;
import com.miracle.miraclemorningback.dto.MemberRequestDto;
import com.miracle.miraclemorningback.dto.MemberResponseDto;
import com.miracle.miraclemorningback.dto.RequestSuccessDto;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.Role;
import com.miracle.miraclemorningback.repository.MemberRepository;
import com.miracle.miraclemorningback.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

        @Autowired
        private MemberRepository memberRepository;

        @Autowired
        private JwtTokenProvider jwtTokenProvider;

        // 전체 회원 조회
        @Transactional(readOnly = true)
        public List<MemberResponseDto> getMembers() {

                return memberRepository.findAll().stream().map(memberEntity -> MemberResponseDto.builder()
                                .memberId(memberEntity.getMemberId())
                                .memberName(memberEntity.getMemberName())
                                .password(memberEntity.getPassword())
                                .role(memberEntity.getRole())
                                .createdAt(memberEntity.getCreatedAt())
                                .build())
                                .toList();
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

        // 특정 회원 검색
        @Transactional
        public ResponseEntity<Object> getMember(String memberName) {

                MemberEntity memberEntity = memberRepository.findByMemberName(memberName)
                                .orElseGet(() -> {
                                        return MemberEntity.builder().memberName(null).build();
                                });

                // 사용자가 존재하지 않으면 UNAUTHORIZED 상태 코드를 반환
                if (memberEntity.getMemberName() == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                                .memberId(memberEntity.getMemberId())
                                .memberName(memberEntity.getMemberName())
                                .password(memberEntity.getPassword())
                                .role(memberEntity.getRole())
                                .createdAt(memberEntity.getCreatedAt())
                                .build();
                return ResponseEntity.ok().body(memberResponseDto);
        }

        // 회원 삭제
        @Transactional
        public ResponseEntity<Object> deleteMember(Long memberId) {
                if (!memberRepository.existsById(memberId)) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당하는 리소스가 없습니다.");
                } else {
                        memberRepository.deleteById(memberId);
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder().success(true).build();
                        return ResponseEntity.ok().body(requestSuccessDto);
                }
        }

        // 로그인
        @Transactional
        public ResponseEntity<Object> loginMember(MemberRequestDto requestDto) {

                MemberEntity memberEntity = memberRepository.findByMemberName(requestDto.getMemberName())
                                .orElseGet(() -> {
                                        return MemberEntity.builder().memberName(null).build();
                                });

                // 닉네임 또는 비밀번호가 일치하지 않으면 UNAUTHORIZED 상태 코드를 반환
                if (memberEntity.getMemberName() == null
                                || !requestDto.getPassword().equals(memberEntity.getPassword())) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder().success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(requestSuccessDto);
                }

                // 승인되지 않은 사용자인 경우 FORBIDDEN 상태 코드를 반환
                if (memberEntity.getRole() != Role.USER) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("승인되지 않은 사용자입니다.");
                }

                TokenDto tokenDto = TokenDto.builder()
                                .accessToken(jwtTokenProvider.generateToken(
                                                memberEntity.getMemberId(),
                                                memberEntity.getMemberName(),
                                                memberEntity.getRole()))
                                .build();

                return ResponseEntity.ok().body(tokenDto);
        }

        /*
         * 1차 배포에는 사용자 닉네임 변경 기능 제외
         * // 회원 정보 수정
         * 
         * @Transactional
         * public MemberResponseDto updateMember(String memberName, MemberRequestDto
         * requestDto) throws Exception {
         * MemberEntity memberEntity =
         * memberRepository.findByMemberName(memberName).orElseThrow(
         * // 사용자명이 존재하지 않으면 예외 처리
         * () -> new IllegalArgumentException("존재하지 않은 사용자입니다."));
         * 
         * // 비밀번호가 일치하지 않으면 예외 처리
         * if (!requestDto.getPassword().equals(memberEntity.getPassword())) {
         * throw new Exception("비밀번호가 일치하지 않습니다.");
         * }
         * 
         * memberRepository.updateMemberName(memberName, requestDto.getMemberName());
         * 
         * return MemberResponseDto.builder()
         * .memberId(memberEntity.getMemberId())
         * .memberName(memberEntity.getMemberName())
         * .password(memberEntity.getPassword())
         * .role(memberEntity.getRole())
         * .createdAt(memberEntity.getCreatedAt())
         * .build();
         * }
         */
}