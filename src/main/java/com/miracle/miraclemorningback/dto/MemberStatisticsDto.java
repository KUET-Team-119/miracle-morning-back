package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberStatisticsDto {

    private Long memberId;
    private String memberName;
    private Long completeResultCount;
    private Long totalResultCount;

    @Builder
    public MemberStatisticsDto(Long memberId, String memberName, Long completeResultCount,
            Long totalResultCount) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.completeResultCount = completeResultCount;
        this.totalResultCount = totalResultCount;
    }
}
