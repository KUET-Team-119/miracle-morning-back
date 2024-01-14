package com.miracle.miraclemorningback.entity;

import com.miracle.miraclemorningback.dto.ResultRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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

    @Column
    private String memberName; // 사용자명

    @Column
    private String routineName; // 루틴명

    @Column
    private String proofFilePath; // 인증 사진 파일 경로

    public ResultEntity(ResultRequestDto requestDto, String filePath) {
        this.memberName = requestDto.getMemberName();
        this.routineName = requestDto.getRoutineName();
        this.proofFilePath = filePath;
    }
}
