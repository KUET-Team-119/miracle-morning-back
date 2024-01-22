package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultDeleteSuccessResponseDto {
    private Boolean success;

    @Builder
    public ResultDeleteSuccessResponseDto(Boolean success) {
        this.success = success;
    }
}
