package com.miracle.miraclemorningback.entity;

import java.sql.Time;

import org.hibernate.annotations.DynamicInsert;

import com.miracle.miraclemorningback.dto.RoutineRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "routine") // database에 해당 이름의 테이블 생성
public class RoutineEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Long routineId; // 루틴 아이디

    @Column
    private String memberName; // 사용자명

    @Column
    private String routineName; // 루틴명

    @Column
    private String strategy; // 실천전략

    @Column(nullable = false)
    private String certification; // 인증방법

    @Temporal(TemporalType.TIME)
    private Time startTime; // 시작시간

    @Temporal(TemporalType.TIME)
    private Time endTime; // 종료시간

    @Column(columnDefinition = "boolean default true")
    private Boolean isActivated; // 활성화여부

    public RoutineEntity(RoutineRequestDto requestDto) {
        this.routineName = requestDto.getRoutineName();
        this.memberName = requestDto.getMemberName();
        this.strategy = requestDto.getStrategy();
        this.certification = requestDto.getCertification();
        this.startTime = requestDto.getStartTime();
        this.endTime = requestDto.getEndTime();
        this.isActivated = requestDto.getIsActivated();
    }
}