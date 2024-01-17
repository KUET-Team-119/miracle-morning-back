package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDto {
    // To-do 불필요한 데이터(isAdmin 등)를 Response하는 것은 아닌지 확인하기
    private Long memberId;
    private String memberName;
    private String password;
    private Boolean isAdmin;
    private LocalDateTime createdAt;

    @Builder
    public MemberResponseDto(Long memberId, String memberName, String password,
            Boolean isAdmin, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.password = password;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
    }
}
