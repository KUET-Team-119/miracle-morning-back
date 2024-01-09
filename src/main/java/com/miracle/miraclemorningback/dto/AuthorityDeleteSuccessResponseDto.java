package com.miracle.miraclemorningback.dto;

import lombok.Getter;

@Getter
public class AuthorityDeleteSuccessResponseDto {
    private Boolean success;

    public AuthorityDeleteSuccessResponseDto(Boolean success) {
        this.success = success;
    }
}
