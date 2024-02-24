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

    private Long resultId;
    private Long routineId;
    private String routineName;
    private String memberName;
    private String dayOfWeek;
    private String certification;
    private Time startTime;
    private Time endTime;
    private LocalDateTime createdAt;
    private LocalDateTime doneAt;
    private Boolean complete;

    @Builder
    public TodayRoutinesDto(Long resultId, Long routineId, String routineName, String memberName, String dayOfWeek,
            String certification, Time startTime, Time endTime, LocalDateTime createdAt, LocalDateTime doneAt,
            Boolean complete) {
        this.resultId = resultId;
        this.routineId = routineId;
        this.routineName = routineName;
        this.memberName = memberName;
        this.dayOfWeek = dayOfWeek;
        this.certification = certification;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
        this.doneAt = doneAt;
        this.complete = complete;
    }
}
