package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.config.JWTTokenGenerator;
import com.miracle.miraclemorningback.dto.MemberDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.MemberRequestDto;
import com.miracle.miraclemorningback.dto.MemberResponseDto;
import com.miracle.miraclemorningback.dto.TokenDto;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    private JWTTokenGenerator jwtTokenGenerator = new JWTTokenGenerator();
    
    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<MemberResponseDto> getMembers() {
        return memberRepository.findAll().stream().map(MemberResponseDto::new).toList();
    }

    // 회원 등록
    public MemberResponseDto registerMember(MemberRequestDto requestDto) {
    	MemberEntity memberEntity = new MemberEntity(requestDto);

        memberRepository.save(memberEntity);
        return new MemberResponseDto(memberEntity);
    }

    // 특정 회원 검색
    @Transactional
    public MemberResponseDto getMember(String memberName) {
        return memberRepository.findByMemberName(memberName).map(MemberResponseDto::new).orElseThrow(
                // 사용자명이 존재하지 않으면 예외 처리
                () -> new UsernameNotFoundException("존재하지 않은 사용자입니다."));
    }

    // 회원 정보 수정
    @Transactional
    public MemberResponseDto updateMember(String memberName, MemberRequestDto requestDto) throws Exception {
        MemberEntity memberEntity = memberRepository.findByMemberName(memberName).orElseThrow(
                // 사용자명이 존재하지 않으면 예외 처리
                () -> new UsernameNotFoundException("존재하지 않은 사용자입니다."));

        // 비밀번호가 일치하지 않으면 예외 처리
        if (!requestDto.getPassword().equals(memberEntity.getPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.updateMemberName(memberName, requestDto.getMemberName());

        return new MemberResponseDto(memberEntity);
    }

    // 회원 삭제
    @Transactional
    public MemberDeleteSuccessResponseDto deleteMember(Long memberId, MemberRequestDto requestDto) throws Exception {
        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(
                // 아이디가 존재하지 않으면 예외 처리
                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));

        // 사용자명이 일치하지 않으면 예외 처리
        if (!requestDto.getMemberName().equals(memberEntity.getMemberName())) {
            throw new UsernameNotFoundException("사용자명이 일치하지 않습니다.");
        }

        memberRepository.deleteById(memberId);
        return new MemberDeleteSuccessResponseDto(true);
    }
    
    @Transactional
	public TokenDto memberLogin(MemberRequestDto requestDto) throws Exception {
		MemberEntity memberEntity = memberRepository.findByMemberName(requestDto.getMemberName()).orElseThrow(
				() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
		
		if (!requestDto.getPassword().equals(memberEntity.getPassword())) {
			throw new Exception("비밀번호가 일치하지 않습니다.");
	    }
		
		TokenDto token = TokenDto.builder()
								 .memberName(memberEntity.getMemberName())
								 .accessToken(jwtTokenGenerator.createToken(memberEntity.getMemberName(),
                                         memberEntity.getRole()))
								 .build();
		return token;
	}
}