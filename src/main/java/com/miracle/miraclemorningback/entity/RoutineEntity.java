package com.miracle.miraclemorningback.entity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Builder;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity; // 사용자

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

    @OneToMany(mappedBy = "routineEntity", cascade = CascadeType.REMOVE)
    private List<ResultEntity> results = new ArrayList<>();

    @Builder
    public RoutineEntity(String routineName, String strategy, String certification, Time startTime, Time endTime,
            Boolean isActivated) {
        this.routineName = routineName;
        this.strategy = strategy;
        this.certification = certification;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActivated = isActivated;
    }

    public void setMemberEntity(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }
}