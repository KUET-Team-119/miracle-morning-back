package com.miracle.miraclemorningback.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member") // database에 해당 이름의 테이블 생성
public class MemberEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId; // 유저 아이디

    @Column(name = "member_name", nullable = false, unique = true, columnDefinition = "varchar(10)")
    private String memberName; // 사용자명

    @Column(nullable = false, columnDefinition = "char(5)")
    private String password; // 비밀번호

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE)
    private List<RoutineEntity> routines = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE)
    private List<ResultEntity> results = new ArrayList<>();

    @Builder
    public MemberEntity(String memberName, String password, Role role) {
        this.memberName = memberName;
        this.password = password;
        this.role = role;
    }
}
