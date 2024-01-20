package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {

    private String memberName;
    private String accessToken;

    @Builder
    public TokenDto(String memberName, String accessToken) {
        this.memberName = memberName;
        this.accessToken = accessToken;
    }
}
