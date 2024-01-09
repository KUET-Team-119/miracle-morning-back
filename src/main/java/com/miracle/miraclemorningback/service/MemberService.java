package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.dto.MemberDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.MemberRequestDto;
import com.miracle.miraclemorningback.dto.MemberResponseDto;
import com.miracle.miraclemorningback.dto.RoutineRequestDto;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service // 스프링이 관리해주는 객체, 스프링 빈
@RequiredArgsConstructor // controller와 같이 final 멤버 변수 생성자를 만드는 역할
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoutineService routineService;

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<MemberResponseDto> getMembers() {
        return memberRepository.findAll().stream().map(MemberResponseDto::new).toList();
    }

    // 회원 등록
    @Transactional
    public MemberResponseDto registerMember(MemberRequestDto requestDto) {
        MemberEntity memberEntity = new MemberEntity(requestDto);

        memberRepository.save(memberEntity);
        return new MemberResponseDto(memberEntity);
    }

    // 특정 회원 검색
    @Transactional
    public MemberResponseDto getMember(String nickname) {
        return memberRepository.findByNickname(nickname).map(MemberResponseDto::new).orElseThrow(
                // 닉네임이 존재하지 않으면 예외 처리
                () -> new IllegalArgumentException("존재하지 않은 닉네임입니다."));
    }

    // 회원 정보 수정
    @Transactional
    public MemberResponseDto updateMember(String nickname, MemberRequestDto requestDto) throws Exception {
        MemberEntity memberEntity = memberRepository.findByNickname(nickname).orElseThrow(
                // 닉네임이 존재하지 않으면 예외 처리
                () -> new IllegalArgumentException("존재하지 않은 닉네임입니다."));

        // 비밀번호가 일치하지 않으면 예외 처리
        if (!requestDto.getPassword().equals(memberEntity.getPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        // 닉네임 변경 쿼리 실행
        // memberEntity.update(requestDto);
        memberRepository.updateNickname(nickname, requestDto.getNickname());
        MemberResponseDto responseDto = new MemberResponseDto(memberEntity);

        // 루틴 테이블에서 사용자 닉네임 변경
        RoutineRequestDto routineRequestDto = new RoutineRequestDto(responseDto);
        routineService.updateNickname(nickname, routineRequestDto);

        return responseDto;
    }

    // 회원 삭제
    @Transactional
    public MemberDeleteSuccessResponseDto deleteMember(Long member_id, MemberRequestDto requestDto) throws Exception {
        MemberEntity memberEntity = memberRepository.findById(member_id).orElseThrow(
                // 아이디가 존재하지 않으면 예외 처리
                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));

        // 닉네임이 일치하지 않으면 예외 처리
        if (!requestDto.getNickname().equals(memberEntity.getNickname())) {
            throw new Exception("닉네임이 일치하지 않습니다.");
        }

        memberRepository.deleteById(member_id);
        return new MemberDeleteSuccessResponseDto(true);
    }
}