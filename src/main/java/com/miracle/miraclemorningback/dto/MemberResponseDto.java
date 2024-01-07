package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;

import com.miracle.miraclemorningback.entity.MemberEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private Long memberId;
    private String nickname;
    private String password;
    private Boolean isAdmin;
    private LocalDateTime createdAt;

    public MemberResponseDto(MemberEntity memberEntity) {
        this.memberId = memberEntity.getMemberId();
        this.nickname = memberEntity.getNickname();
        this.password = memberEntity.getPassword();
        this.isAdmin = memberEntity.getIsAdmin();
        this.createdAt = memberEntity.getCreatedAt();
    }
}
