package com.miracle.miraclemorningback.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.miracle.miraclemorningback.dto.MemberDTO;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service // 스프링이 관리해주는 객체, 스프링 빈
@RequiredArgsConstructor // controller와 같이 final 멤버 변수 생성자를 만드는 역할
public class MemberService {

    private final MemberRepository memberRepository;

    public void addMember(MemberDTO memberDTO) {
        // repsitory의 save 메서드 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        // repository의 save메서드 호출 (인자로 entity객체를 넘겨줘야 함)
        memberRepository.save(memberEntity);
    }

    public MemberDTO signIn(MemberDTO memberDTO) { // entity 객체는 service에서만
        Optional<MemberEntity> byNickname = memberRepository.findByNickname(memberDTO.getNickname());
        if (byNickname.isPresent()) { // 조회 결과가 있는 경우
            MemberEntity memberEntity = byNickname.get(); // Optional에서 꺼냄
            if (memberEntity.getPassword().equals(memberDTO.getPassword())) {
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                // 비밀번호 불일치
                return null;
            }
        } else { // 조회 결과가 없는 경우
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        // Controller에 dto로 변환해서 넘기기
        List<MemberDTO> memberDTOList = new ArrayList<>();

        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }

        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        // 하나 조회할 때 optional로 감싸기
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public void deleteByid(Long id) {
        memberRepository.deleteById(id);
    }
}
