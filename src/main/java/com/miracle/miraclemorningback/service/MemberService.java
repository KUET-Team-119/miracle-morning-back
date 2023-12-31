package com.miracle.miraclemorningback.service;

import org.springframework.stereotype.Service;
import com.miracle.miraclemorningback.dto.MemberDTO;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service // 스프링이 관리해주는 객체, 스프링 빈
@RequiredArgsConstructor // controller와 같이 final 멤버 변수 생성자를 만드는 역할
public class MemberService {

    private final MemberRepository memberRepository;

    public void addUser(MemberDTO memberDTO) {
        // repsitory의 save 메서드 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        // repository의 save메서드 호출 (인자로 entity객체를 넘겨줘야 함)
        memberRepository.save(memberEntity);
    }

}
