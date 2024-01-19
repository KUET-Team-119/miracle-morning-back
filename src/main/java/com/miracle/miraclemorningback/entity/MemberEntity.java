package com.miracle.miraclemorningback.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member") // database에 해당 이름의 테이블 생성
public class MemberEntity extends Timestamped { // table 역할
    // jpa ==> database를 객체처럼 사용 가능

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId; // 유저 아이디

    @Column(nullable = false, unique = true, columnDefinition = "varchar(10)")
    private String memberName; // 사용자명

    @Column(nullable = false, columnDefinition = "char(5)")
    private String password; // 비밀번호

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public MemberEntity(String memberName, String password, Role role) {
        this.memberName = memberName;
        this.password = password;
        this.role = role;
    }
}
