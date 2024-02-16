package com.miracle.miraclemorningback.dto;

import com.miracle.miraclemorningback.entity.Role;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDto {
    private Long memberId; // 사용자 아이디
    private String memberName; // 닉네임
    private String password; // 비밀번호
    private Role role;
}