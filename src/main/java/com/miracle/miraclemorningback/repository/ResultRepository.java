package com.miracle.miraclemorningback.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miracle.miraclemorningback.dto.RoutineAchievementDto;
import com.miracle.miraclemorningback.dto.TodayRoutinesDto;
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

        // [JPQL] 특정 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 기록 조회
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.TodayRoutinesDto(ro.routineId, ro.routineName, re.memberEntity.memberName, ro.dayOfWeek, ro.certification, ro.startTime, ro.endTime, re.createdAt, re.doneAt, false) FROM ResultEntity re JOIN re.routineEntity ro WHERE re.memberEntity = :member_entity AND DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NULL")
        List<TodayRoutinesDto> getActivatedAndIncompleteRoutines(@Param("member_entity") MemberEntity memberEntity);

        // [JPQL] 특정 사용자의 루틴 중 활성화되고 인증된 루틴 기록 조회
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.TodayRoutinesDto(ro.routineId, ro.routineName, re.memberEntity.memberName, ro.dayOfWeek, ro.certification, ro.startTime, ro.endTime, re.createdAt, re.doneAt, true) FROM ResultEntity re JOIN re.routineEntity ro WHERE re.memberEntity = :member_entity AND DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NOT NULL")
        List<TodayRoutinesDto> getActivatedAndCompleteRoutines(@Param("member_entity") MemberEntity memberEntity);

        // [JPQL] 모든 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 기록 조회
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.TodayRoutinesDto(ro.routineId, ro.routineName, re.memberEntity.memberName, ro.dayOfWeek, ro.certification, ro.startTime, ro.endTime, re.createdAt, re.doneAt, false) FROM ResultEntity re JOIN re.routineEntity ro WHERE DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NULL")
        List<TodayRoutinesDto> getAllActivatedAndIncompleteRoutines();

        // [JPQL] 모든 사용자의 루틴 중 활성화되고 인증된 루틴 기록 조회
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.TodayRoutinesDto(ro.routineId, ro.routineName, re.memberEntity.memberName, ro.dayOfWeek, ro.certification, ro.startTime, ro.endTime, re.createdAt, re.doneAt, true) FROM ResultEntity re JOIN re.routineEntity ro WHERE DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NOT NULL")
        List<TodayRoutinesDto> getAllActivatedAndCompleteRoutines();

        // 요일별 달성률
        @Query("SELECT CEIL(COALESCE((COUNT(CASE WHEN re.doneAt IS NOT NULL THEN 1 END) / COUNT(re.resultId) * 100), 0)) FROM ResultEntity re WHERE re.memberEntity = :member_entity AND FUNCTION('WEEKDAY', re.createdAt) = :day_of_week")
        Integer getDayOfWeekAchievement(
                        @Param("member_entity") MemberEntity memberEntity,
                        @Param("day_of_week") int dayOfWeek);

        // 루틴별 달성률
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.RoutineAchievementDto(ro.routineName, CEIL(COALESCE((COUNT(CASE WHEN re.doneAt IS NOT NULL THEN 1 END) / COUNT(re.resultId) * 100), 0))) FROM ResultEntity re JOIN re.routineEntity ro WHERE re.memberEntity = :member_entity GROUP BY ro.routineName")
        List<RoutineAchievementDto> getRoutineAchievement(@Param("member_entity") MemberEntity memberEntity);
}