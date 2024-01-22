package com.miracle.miraclemorningback.dto;

import java.sql.Time;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayRoutinesDto {

    private Long routineId;
    private String routineName;
    private String memberName;
    private String strategy;
    private String certification;
    private Time startTime;
    private Time endTime;
    private LocalDateTime createdAt;
    private Boolean complete;

    @Builder
    public TodayRoutinesDto(Long routineId, String routineName, String memberName, String strategy,
            String certification, Time startTime, Time endTime, LocalDateTime createdAt, Boolean complete) {
        this.routineId = routineId;
        this.routineName = routineName;
        this.memberName = memberName;
        this.strategy = strategy;
        this.certification = certification;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
        this.complete = complete;
    }
}
