package com.miracle.miraclemorningback.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "result")
public class ResultEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId; // 기록 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity; // 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private RoutineEntity routineEntity; // 루틴

    @Column
    private String proofFilePath; // 인증 사진 파일 경로

    @Column
    private LocalDateTime doneAt; // 사진 촬영 시간

    @Builder
    public ResultEntity(String filePath, LocalDateTime doneAt) {
        this.proofFilePath = filePath;
        this.doneAt = doneAt;
    }

    public void setMemberEntity(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }

    public void setRoutineEntity(RoutineEntity routineEntity) {
        this.routineEntity = routineEntity;
    }
}
