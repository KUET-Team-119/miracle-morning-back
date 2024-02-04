package com.miracle.miraclemorningback.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miracle.miraclemorningback.entity.ResultEntity;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, Long> {

    // 오늘 날짜의 기록만 조회
    @Query("SELECT re FROM ResultEntity re WHERE DATE(re.createdAt) = CURRENT_DATE")
    List<ResultEntity> findAllByCurrentDate();

    // 인증할 루틴 기록 조회
    @Query("SELECT re FROM ResultEntity re WHERE re.routineName = :routine_name AND re.memberName = :member_name AND DATE(re.createdAt) = CURRENT_DATE")
    Optional<ResultEntity> findByRoutineNameAndMemberNameAndCurrentDate(@Param("routine_name") String routineName,
            @Param("member_name") String memberName);

    // 인증한 루틴 기록 업데이트
    @Modifying
    @Query("UPDATE ResultEntity re SET re.doneAt = :done_at, proofFilePath = :proof_file_path WHERE re.routineName = :routine_name AND re.memberName = :member_name AND DATE(re.createdAt) = CURRENT_DATE")
    void updateResult(@Param("routine_name") String routineName,
            @Param("member_name") String memberName,
            @Param("done_at") LocalDateTime doneAt,
            @Param("proof_file_path") String proofFilePath);
}