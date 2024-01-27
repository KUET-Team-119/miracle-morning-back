package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComplaintResponseDto {
    private Long complaintId;
    private String memberName;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public ComplaintResponseDto(Long complaintId, String memberName, String content, LocalDateTime createdAt) {
        this.complaintId = complaintId;
        this.memberName = memberName;
        this.content = content;
        this.createdAt = createdAt;
    }
}
