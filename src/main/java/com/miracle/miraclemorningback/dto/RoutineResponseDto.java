package com.miracle.miraclemorningback.dto;

import java.sql.Time;
import java.time.LocalDateTime;

import com.miracle.miraclemorningback.entity.RoutineEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineResponseDto {
    private String routineName;
    private String nickname;
    private String strategy;
    private String certification;
    private Time startTime;
    private Time endTime;
    private Boolean isActivated;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public RoutineResponseDto(RoutineEntity routineEntity) {
        this.routineName = routineEntity.getRoutineName();
        this.nickname = routineEntity.getNickname();
        this.strategy = routineEntity.getStrategy();
        this.certification = routineEntity.getCertification();
        this.startTime = routineEntity.getStartTime();
        this.endTime = routineEntity.getEndTime();
        this.isActivated = routineEntity.getIsActivated();
        this.createdAt = routineEntity.getCreatedAt();
        this.modifiedAt = routineEntity.getModifiedAt();
    }
}
