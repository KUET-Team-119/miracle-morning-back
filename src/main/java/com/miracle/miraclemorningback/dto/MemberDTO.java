package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;
import com.miracle.miraclemorningback.entity.MemberEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//lombok dependency추가
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO { // 회원 정보를 필드로 정의
    private Long id; // 아이디
    private String nickname; // 닉네임
    private String password; // 비밀번호
    private LocalDateTime joinDate; // 가입일자
    private Boolean isAdmin; // 관리자여부

    // lombok 어노테이션으로 getter,setter,생성자,toString 메서드 생략

    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setNickname(memberEntity.getNickname());
        memberDTO.setPassword(memberEntity.getPassword());
        memberDTO.setJoinDate(memberEntity.getJoinDate());
        memberDTO.setIsAdmin(memberEntity.getIsAdmin());

        return memberDTO;
    }
}