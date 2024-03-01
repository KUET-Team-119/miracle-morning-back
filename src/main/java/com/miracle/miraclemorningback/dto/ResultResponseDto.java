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
    private Long routineId;
    private String routineName;
    private Long memberId;
    private String memberName;
    private LocalDateTime createdAt;
    private LocalDateTime doneAt;
    // private LocalDateTime memberJoinDate;
    private String fileBase64;

    @Builder
    public ResultResponseDto(
            Long resultId,
            Long routineId,
            String routineName,
            Long memberId,
            String memberName,
            LocalDateTime createdAt,
            LocalDateTime doneAt,
            // LocalDateTime memberJoinDate,
            String fileBase64) {
        this.resultId = resultId;
        this.routineId = routineId;
        this.routineName = routineName;
        this.memberId = memberId;
        this.memberName = memberName;
        this.createdAt = createdAt;
        this.doneAt = doneAt;
        // this.memberJoinDate = memberJoinDate;
        this.fileBase64 = fileBase64;
    }
}