package com.miracle.miraclemorningback.entity;

import java.sql.Time;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayRoutinesEntity {

    // nativeQuery에서 결과를 받아오기 때문에 스네이크 케이스로 표기

    @Id
    @Column
    private Long routine_id; // 루틴 아이디

    @Column
    private String routine_name; // 루틴명

    @Column
    private String member_name; // 닉네임

    @Column
    private String strategy; // 실천전략

    @Column
    private String certification; // 인증방법

    @Column
    private Time start_time; // 시작시간

    @Column
    private Time end_time; // 종료시간

    @Column
    private LocalDateTime created_at; // 생성시간

    @Column
    private LocalDateTime done_at; // 사진 촬영 시간

    @Builder
    public TodayRoutinesEntity(Long routineId, String routineName, String memberName, String strategy,
            String certification, Time startTime, Time endTime, LocalDateTime createdAt, LocalDateTime doneAt) {
        this.routine_id = routineId;
        this.routine_name = routineName;
        this.member_name = memberName;
        this.strategy = strategy;
        this.certification = certification;
        this.start_time = startTime;
        this.end_time = endTime;
        this.created_at = createdAt;
        this.done_at = doneAt;
    }
}
