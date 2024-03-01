package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;

import com.miracle.miraclemorningback.entity.Role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDto {
    private Long memberId;
    private String memberName;
    private String password;
    private Role role;
    private LocalDateTime createdAt;

    @Builder
    public MemberResponseDto(Long memberId, String memberName, String password, Role role,
            LocalDateTime createdAt) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }
}
