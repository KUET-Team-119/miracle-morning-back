package com.miracle.miraclemorningback.dto;

import lombok.Getter;

@Getter
public class ResultDeleteSuccessResponseDto {
    private Boolean success;

    public ResultDeleteSuccessResponseDto(Boolean success) {
        this.success = success;
    }
}
