package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestSuccessDto {
    private Boolean success;
    private String message;

    @Builder
    public RequestSuccessDto(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
