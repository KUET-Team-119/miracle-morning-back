package com.miracle.miraclemorningback.dto;

import java.sql.Time;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineResponseDto {
    private Long routineId;
    private String routineName;
    private String memberName;
    private String dayOfWeek;
    private String certification;
    private Time startTime;
    private Time endTime;
    private Boolean isActivated;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public RoutineResponseDto(Long routineId, String routineName, String memberName, String dayOfWeek,
            String certification, Time startTime, Time endTime, Boolean isActivated, LocalDateTime createdAt,
            LocalDateTime modifiedAt) {
        this.routineId = routineId;
        this.routineName = routineName;
        this.memberName = memberName;
        this.dayOfWeek = dayOfWeek;
        this.certification = certification;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActivated = isActivated;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
