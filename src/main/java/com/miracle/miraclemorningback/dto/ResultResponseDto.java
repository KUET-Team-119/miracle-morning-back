package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;

import com.miracle.miraclemorningback.entity.ResultEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultResponseDto {
    private String routineName;
    private String nickname;
    private LocalDateTime createdAt;

    public ResultResponseDto(ResultEntity resultEntity) {
        this.routineName = resultEntity.getRoutineName();
        this.nickname = resultEntity.getNickName();
        this.createdAt = resultEntity.getCreatedAt();
    }
}
