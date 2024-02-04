package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultRequestDto {
    private String routineName; // 루틴명
    private String memberName; // 사용자명
    private LocalDateTime doneAt; // 사진 촬영 시간
}
