package com.miracle.miraclemorningback.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "complaint")
public class ComplaintEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    private Long complaintId; // 컴플레인 아이디

    @Column
    private String memberName; // 제보자

    @Column
    private String content; // 제보 내용

    @Builder
    public ComplaintEntity(String memberName, String content) {
        this.memberName = memberName;
        this.content = content;
    }
}
