package com.miracle.miraclemorningback.dto;

import java.sql.Time;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineRequestDto {
    private String routineName;
    private String nickname;
    private String strategy;
    private String certification;
    private Time startTime;
    private Time endTime;
    private Boolean isActivated;

    public RoutineRequestDto(MemberResponseDto responseDto) {
        this.nickname = responseDto.getNickname();
    }
}
