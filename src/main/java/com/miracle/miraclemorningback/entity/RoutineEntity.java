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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@DynamicInsert
@NoArgsConstructor
@Table(name = "routine") // database에 해당 이름의 테이블 생성
public class RoutineEntity extends Timestamped {

    // @EmbeddedId
    // private RoutinePK routinePK;

    // @MapsId("memberId")
    // @ManyToOne
    // @JoinColumn(name = "MEMBER_ID")
    // private MemberEntity memberEntity;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROUTINE_ID")
    private Long routineId; // 루틴 아이디

    @Column
    private String nickname; // 닉네임

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
        // this.routinePK.setRoutineName(requestDto.getRoutineName());
        this.routineName = requestDto.getRoutineName();
        this.nickname = requestDto.getNickname();
        this.strategy = requestDto.getStrategy();
        this.certification = requestDto.getCertification();
        this.startTime = requestDto.getStartTime();
        this.endTime = requestDto.getEndTime();
        this.isActivated = requestDto.getIsActivated();
    }

    public void update(RoutineRequestDto requestDto) {
        // this.routinePK.setRoutineName(requestDto.getRoutineName());
        this.routineName = requestDto.getRoutineName();
        this.nickname = requestDto.getNickname();
        this.strategy = requestDto.getStrategy();
        this.certification = requestDto.getCertification();
        this.startTime = requestDto.getStartTime();
        this.endTime = requestDto.getEndTime();
        this.isActivated = requestDto.getIsActivated();
    }
}