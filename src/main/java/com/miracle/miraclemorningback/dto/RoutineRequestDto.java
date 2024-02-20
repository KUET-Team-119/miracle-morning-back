package com.miracle.miraclemorningback.dto;

import java.sql.Time;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineRequestDto {
    private Long routineId;
    private String routineName;
    private String memberName;
    private String certification;
    private Time startTime;
    private Time endTime;
    private Boolean isActivated;
}
