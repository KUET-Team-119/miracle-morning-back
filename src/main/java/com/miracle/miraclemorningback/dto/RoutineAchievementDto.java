package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineAchievementDto {

    private String routineName; // 루틴명

    private Object achievement; // 달성률

    @Builder
    public RoutineAchievementDto(String routineName, Object achievement) {
        this.routineName = routineName;
        this.achievement = achievement;
    }
}
