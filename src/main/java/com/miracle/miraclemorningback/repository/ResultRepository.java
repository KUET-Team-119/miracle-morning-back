package com.miracle.miraclemorningback.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.ResultEntity;
import com.miracle.miraclemorningback.entity.RoutineEntity;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, Long> {

        // 특정 사용자의 특정 기간 기록 검색
        @Query("SELECT re FROM ResultEntity re WHERE re.memberEntity = :member_entity AND YEAR(re.createdAt) = :year AND MONTH(re.createdAt) = :month")
        List<ResultEntity> findAllByIdAndYearAndMonth(
                        @Param("member_entity") MemberEntity memberEntity,
                        @Param("year") Integer year,
                        @Param("month") Integer month);

        // 오늘 날짜의 기록 조회
        @Query("SELECT re FROM ResultEntity re WHERE DATE(re.createdAt) = CURRENT_DATE")
        List<ResultEntity> findAllByCurrentDate();

        // 인증할 루틴 기록 조회
        @Query("SELECT re FROM ResultEntity re WHERE re.routineEntity = :routine_entity AND re.memberEntity = :member_entity AND DATE(re.createdAt) = CURRENT_DATE")
        Optional<ResultEntity> findByRoutineEntityAndMemberEntityAndCurrentDate(
                        @Param("routine_entity") RoutineEntity routineEntity,
                        @Param("member_entity") MemberEntity member_entity);

        // 인증한 루틴 기록 업데이트
        @Modifying
        @Query("UPDATE ResultEntity re SET re.doneAt = :done_at, proofFilePath = :proof_file_path WHERE re.routineEntity = :routine_entity AND re.memberEntity = :member_entity AND DATE(re.createdAt) = CURRENT_DATE")
        void updateResult(
                        @Param("routine_entity") RoutineEntity routineEntity,
                        @Param("member_entity") MemberEntity member_entity,
                        @Param("done_at") LocalDateTime doneAt,
                        @Param("proof_file_path") String proofFilePath);
}