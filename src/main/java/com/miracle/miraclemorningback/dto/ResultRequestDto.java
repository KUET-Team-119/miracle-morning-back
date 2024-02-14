package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultRequestDto {
    private Long routineId; // 루틴 아이디
    private String memberName; // 사용자명
    private LocalDateTime doneAt; // 사진 촬영 시간
}
