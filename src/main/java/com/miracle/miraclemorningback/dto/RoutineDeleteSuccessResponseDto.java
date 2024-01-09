package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineDeleteSuccessResponseDto {
    private Boolean success;

    public RoutineDeleteSuccessResponseDto(Boolean success) {
        this.success = success;
    }
}