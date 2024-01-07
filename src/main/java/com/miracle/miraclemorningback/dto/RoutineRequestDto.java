package com.miracle.miraclemorningback.dto;

import java.sql.Time;

import lombok.Getter;

@Getter
public class RoutineRequestDto {
    private String routineName;
    private String nickname;
    private String strategy;
    private String certification;
    private Time startTime;
    private Time endTime;
    private Boolean isActivated;
}
