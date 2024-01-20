package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.dto.TokenDto;
import com.miracle.miraclemorningback.dto.MemberDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.MemberRequestDto;
import com.miracle.miraclemorningback.dto.MemberResponseDto;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.Role;
import com.miracle.miraclemorningback.repository.MemberRepository;
import com.miracle.miraclemorningback.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service // 스프링이 관리해주는 객체, 스프링 빈
@RequiredArgsConstructor // controller와 같이 final 멤버 변수 생성자를 만드는 역할
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
        public MemberResponseDto registerMember(MemberRequestDto requestDto) {
                MemberEntity memberEntity = MemberEntity.builder()
                                .memberName(requestDto.getMemberName())
                                .password(requestDto.getPassword())
                                .role(Role.ROLE_TEMPORARY_USER)
                                .build();

                memberRepository.save(memberEntity);

                return MemberResponseDto.builder()
                                .memberId(memberEntity.getMemberId())
                                .memberName(memberEntity.getMemberName())
                                .password(memberEntity.getPassword())
                                .role(memberEntity.getRole())
                                .createdAt(memberEntity.getCreatedAt())
                                .build();
        }

        // 특정 회원 검색
        @Transactional
        public MemberResponseDto getMember(String memberName) {
                return memberRepository.findByMemberName(memberName)
                                .map(memberEntity -> MemberResponseDto.builder()
                                                .memberId(memberEntity.getMemberId())
                                                .memberName(memberEntity.getMemberName())
                                                .password(memberEntity.getPassword())
                                                .role(memberEntity.getRole())
                                                .createdAt(memberEntity.getCreatedAt())
                                                .build())
                                .orElseThrow(
                                                // 사용자명이 존재하지 않으면 예외 처리
                                                () -> new IllegalArgumentException("존재하지 않은 사용자입니다."));
        }

        // 회원 정보 수정
        @Transactional
        public MemberResponseDto updateMember(String memberName, MemberRequestDto requestDto) throws Exception {
                MemberEntity memberEntity = memberRepository.findByMemberName(memberName).orElseThrow(
                                // 사용자명이 존재하지 않으면 예외 처리
                                () -> new IllegalArgumentException("존재하지 않은 사용자입니다."));

                // 비밀번호가 일치하지 않으면 예외 처리
                if (!requestDto.getPassword().equals(memberEntity.getPassword())) {
                        throw new Exception("비밀번호가 일치하지 않습니다.");
                }

                memberRepository.updateMemberName(memberName, requestDto.getMemberName());

                return MemberResponseDto.builder()
                                .memberId(memberEntity.getMemberId())
                                .memberName(memberEntity.getMemberName())
                                .password(memberEntity.getPassword())
                                .role(memberEntity.getRole())
                                .createdAt(memberEntity.getCreatedAt())
                                .build();
        }

        // 회원 삭제
        @Transactional
        public MemberDeleteSuccessResponseDto deleteMember(Long memberId, MemberRequestDto requestDto)
                        throws Exception {
                MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(
                                // 아이디가 존재하지 않으면 예외 처리
                                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));

                // 사용자명이 일치하지 않으면 예외 처리
                if (!requestDto.getMemberName().equals(memberEntity.getMemberName())) {
                        throw new Exception("사용자명이 일치하지 않습니다.");
                }

                memberRepository.deleteById(memberId);
                return new MemberDeleteSuccessResponseDto(true);
        }

        // 로그인
        @Transactional
        public TokenDto loginMember(MemberRequestDto requestDto) throws Exception {
                MemberEntity memberEntity = memberRepository.findByMemberName(requestDto.getMemberName()).orElseThrow(
                                // 사용자명이 일치하지 않으면 예외 처리
                                () -> new IllegalArgumentException("존재하지 않은 사용자입니다."));

                // 비밀번호가 일치하지 않으면 예외 처리
                if (!requestDto.getPassword().equals(memberEntity.getPassword())) {
                        throw new Exception("비밀번호가 일치하지 않습니다.");
                }

                TokenDto tokenDto = TokenDto.builder()
                                .memberName(memberEntity.getMemberName())
                                .accessToken(jwtTokenProvider.generateToken(memberEntity.getMemberName(),
                                                memberEntity.getRole()))
                                .build();

                return tokenDto;
        }
}