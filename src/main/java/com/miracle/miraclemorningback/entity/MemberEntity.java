package com.miracle.miraclemorningback.entity;

import org.hibernate.annotations.DynamicInsert;

import com.miracle.miraclemorningback.dto.MemberRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member") // database에 해당 이름의 테이블 생성
public class MemberEntity extends Timestamped { // table 역할
    // jpa ==> database를 객체처럼 사용 가능

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long memberId; // 유저 아이디

    @Column(nullable = false, unique = true, columnDefinition = "varchar(10)")
    private String nickname; // 닉네임

    @Column(nullable = false, columnDefinition = "char(6)")
    private String password; // 비밀번호

    @Column(columnDefinition = "boolean default false")
    private Boolean isAdmin; // 관리자여부

    public MemberEntity(MemberRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.password = requestDto.getPassword();
    }

    public void update(MemberRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
    }
}
