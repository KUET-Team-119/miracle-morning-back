package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResultDto {

    private String token;

    @Builder
    public LoginResultDto(String token) {
        this.token = token;
    }
}
