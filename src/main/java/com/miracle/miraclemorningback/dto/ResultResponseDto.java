package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultResponseDto {
    private Long resultId;
    private String routineName;
    private String memberName;
    private LocalDateTime createdAt;

    @Builder
    public ResultResponseDto(Long resultId, String routineName, String memberName, LocalDateTime createdAt) {
        this.resultId = resultId;
        this.routineName = routineName;
        this.memberName = memberName;
        this.createdAt = createdAt;
    }
}
