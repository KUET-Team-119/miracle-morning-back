package com.miracle.miraclemorningback.dto;

import com.miracle.miraclemorningback.entity.AuthorityEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorityResponseDto {
    private Long authorityId;
    private String authorityName;

    public AuthorityResponseDto(AuthorityEntity authorityEntity) {
        this.authorityId = authorityEntity.getAuthorityId();
        this.authorityName = authorityEntity.getAuthorityName();
    }
}
