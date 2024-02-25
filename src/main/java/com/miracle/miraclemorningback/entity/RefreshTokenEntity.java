package com.miracle.miraclemorningback.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@RedisHash(value = "refreshToken", timeToLive = 60 * 2) // 5분 동안 유효
public class RefreshTokenEntity {

    @Id
    private String refreshToken;

    @Indexed
    private String accessToken;

    private Long memberId;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
