package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginSuccessResponseDto {
    private Boolean success;

    public MemberLoginSuccessResponseDto(Boolean success) {
        this.success = success;
    }
}
