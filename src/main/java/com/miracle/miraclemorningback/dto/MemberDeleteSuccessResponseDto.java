package com.miracle.miraclemorningback.dto;

import lombok.Getter;

@Getter
public class MemberDeleteSuccessResponseDto {
    private Boolean success;

    public MemberDeleteSuccessResponseDto(Boolean success) {
        this.success = success;
    }
}
