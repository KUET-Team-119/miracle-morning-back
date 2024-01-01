package com.miracle.miraclemorningback.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.miracle.miraclemorningback.dto.MemberDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@DynamicInsert
@Table(name = "member_table") // database에 해당 이름의 테이블 생성
public class MemberEntity { // table 역할
    // jpa ==> database를 객체처럼 사용 가능

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(unique = true)
    private String nickname;

    @Column
    private String password;

    @CreationTimestamp
    @Column
    private LocalDateTime joinDate;

    @Column(columnDefinition = "boolean default false")
    private Boolean isAdmin;

    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setNickname(memberDTO.getNickname());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setJoinDate(memberDTO.getJoinDate());
        memberEntity.setIsAdmin(memberDTO.getIsAdmin());

        return memberEntity;
    }
}
