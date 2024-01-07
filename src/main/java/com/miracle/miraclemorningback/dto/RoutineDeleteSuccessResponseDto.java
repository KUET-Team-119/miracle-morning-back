package com.miracle.miraclemorningback.dto;

import lombok.Getter;

@Getter
public class RoutineDeleteSuccessResponseDto {
    private Boolean success;

    public RoutineDeleteSuccessResponseDto(Boolean success) {
        this.success = success;
    }
}
