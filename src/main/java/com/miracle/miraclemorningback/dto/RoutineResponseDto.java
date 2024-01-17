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
	private Long routineId;
    private String routineName;
    private String memberName;
    private String strategy;
    private String certification;
    private Time startTime;
    private Time endTime;
    private Boolean isActivated;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public RoutineResponseDto(RoutineEntity routineEntity) {
        this.routineId = routineEntity.getRoutineId();
    	this.routineName = routineEntity.getRoutineName();
        this.memberName = routineEntity.getMemberName();
        this.strategy = routineEntity.getStrategy();
        this.certification = routineEntity.getCertification();
        this.startTime = routineEntity.getStartTime();
        this.endTime = routineEntity.getEndTime();
        this.isActivated = routineEntity.getIsActivated();
        this.createdAt = routineEntity.getCreatedAt();
        this.modifiedAt = routineEntity.getModifiedAt();
    }
}
