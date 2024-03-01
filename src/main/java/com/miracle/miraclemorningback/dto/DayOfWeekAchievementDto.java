package com.miracle.miraclemorningback.dto;

import com.miracle.miraclemorningback.entity.DayOfWeek;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DayOfWeekAchievementDto {

    private DayOfWeek dayOfWeek; // 요일

    private Integer achievement; // 달성률

    @Builder
    public DayOfWeekAchievementDto(DayOfWeek dayOfWeek, Integer achievement) {
        this.dayOfWeek = dayOfWeek;
        this.achievement = achievement;
    }
}
