package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;

import com.miracle.miraclemorningback.entity.MemberEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDto {
    private Long memberId;
    private String nickname;
    private String password;
    private Boolean isAdmin;
    private LocalDateTime createdAt;

    // 빌더 패턴
    // @Builder
    // public MemberResponseDto(Long memerId, String nickname, String password,
    // Boolean isAdmin, LocalDateTime createdAt) {
    // this.memberId = memerId;
    // this.nickname = nickname;
    // this.password = password;
    // this.isAdmin = isAdmin;
    // this.createdAt = createdAt;
    // }

    // public MemberResponseDto(Long memberId, String nicknme, String password,
    // Boolean isAdmin, LocalDateTime createdAt) {
    // this.memberId = memberId;
    // this.nickname = nicknme;
    // this.password = password;
    // this.isAdmin = isAdmin;
    // this.createdAt = createdAt;
    // }

    public MemberResponseDto(MemberEntity memberEntity) {
        this.memberId = memberEntity.getMemberId();
        this.nickname = memberEntity.getNickname();
        this.password = memberEntity.getPassword();
        this.isAdmin = memberEntity.getIsAdmin();
        this.createdAt = memberEntity.getCreatedAt();
    }
}
