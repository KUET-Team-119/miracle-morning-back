package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDeleteSuccessResponseDto {
    private Boolean success;

    public MemberDeleteSuccessResponseDto(Boolean success) {
        this.success = success;
    }
}
