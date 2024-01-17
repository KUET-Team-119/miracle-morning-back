package com.miracle.miraclemorningback.dto;

import java.time.LocalDateTime;

import com.miracle.miraclemorningback.entity.ResultEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultResponseDto {
	private Long resultId;
    private String routineName;
    private String memberName;
    private LocalDateTime createdAt;

    public ResultResponseDto(ResultEntity resultEntity) {
        this.resultId = resultEntity.getResultId();
    	this.routineName = resultEntity.getRoutineName();
        this.memberName = resultEntity.getMemberName();
        this.createdAt = resultEntity.getCreatedAt();
    }
}
