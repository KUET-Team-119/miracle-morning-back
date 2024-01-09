package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultRequestDto {
    private String routineName; // 루틴명
    private String nickname; // 닉네임
}