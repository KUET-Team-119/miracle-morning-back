package com.miracle.miraclemorningback.entity;

import org.hibernate.annotations.DynamicInsert;

import com.miracle.miraclemorningback.dto.MemberRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member") // database에 해당 이름의 테이블 생성
public class MemberEntity extends Timestamped { // table 역할
    // jpa ==> database를 객체처럼 사용 가능

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId; // 유저 아이디

    @Column(nullable = false, unique = true, columnDefinition = "varchar(10)")
    private String nickname; // 닉네임

    @Column(nullable = false, columnDefinition = "char(6)")
    private String password; // 비밀번호

    @Column(columnDefinition = "boolean default false")
    private Boolean isAdmin; // 관리자여부

    // 빌더 패턴
    // @Builder
    // public MemberEntity(String nickname, String password) {
    // this.nickname = nickname;
    // this.password = password;
    // }

    // public void update(MemberRequestDto requestDto) {
    // this.nickname = requestDto.getNickname();
    // }

    public MemberEntity(MemberRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.password = requestDto.getPassword();
    }
}
