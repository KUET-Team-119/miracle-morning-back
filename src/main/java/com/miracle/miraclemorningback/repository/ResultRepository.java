package com.miracle.miraclemorningback.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miracle.miraclemorningback.dto.MemberStatisticsDto;
import com.miracle.miraclemorningback.dto.RoutineAchievementDto;
import com.miracle.miraclemorningback.dto.TodayRoutinesDto;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.ResultEntity;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, Long> {

        // 특정 사용자의 특정 기간 기록 검색
        @Query("SELECT re FROM ResultEntity re WHERE re.memberEntity = :member_entity AND YEAR(re.createdAt) = :year AND MONTH(re.createdAt) = :month")
        List<ResultEntity> findAllByIdAndYearAndMonth(
                        @Param("member_entity") MemberEntity memberEntity,
                        @Param("year") Integer year,
                        @Param("month") Integer month);

        // 사용자가 인증한 기록 중 오늘 날짜의 기록 조회
        @Query("SELECT re FROM ResultEntity re WHERE re.doneAt IS NOT NULL AND DATE(re.createdAt) = CURRENT_DATE ORDER BY re.doneAt DESC")
        List<ResultEntity> getCompleteAndTodayResults();

        // 사용자가 인증한 기록 중 오늘을 포함해서 4일 전까지의 기록 조회
        @Query("SELECT re FROM ResultEntity re WHERE re.doneAt IS NOT NULL AND FUNCTION('DATEDIFF', CURRENT_DATE, re.createdAt) < 5 ORDER BY re.doneAt DESC")
        List<ResultEntity> getCompleteAndRecentResults();

        // 인증한 루틴 기록 업데이트
        @Modifying
        @Query("UPDATE ResultEntity re SET re.doneAt = :done_at, proofFilePath = :proof_file_path WHERE re.resultId = :result_id")
        void updateResult(
                        @Param("result_id") Long resultId,
                        @Param("done_at") LocalDateTime doneAt,
                        @Param("proof_file_path") String proofFilePath);

        // 특정 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 기록 조회
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.TodayRoutinesDto(re.resultId, ro.routineId, ro.routineName, re.memberEntity.memberName, ro.dayOfWeek, ro.certification, ro.startTime, ro.endTime, re.createdAt, re.doneAt, false) FROM ResultEntity re JOIN re.routineEntity ro WHERE re.memberEntity = :member_entity AND DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NULL")
        List<TodayRoutinesDto> getActivatedAndIncompleteRoutines(@Param("member_entity") MemberEntity memberEntity);

        // 특정 사용자의 루틴 중 활성화되고 인증된 루틴 기록 조회
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.TodayRoutinesDto(re.resultId, ro.routineId, ro.routineName, re.memberEntity.memberName, ro.dayOfWeek, ro.certification, ro.startTime, ro.endTime, re.createdAt, re.doneAt, true) FROM ResultEntity re JOIN re.routineEntity ro WHERE re.memberEntity = :member_entity AND DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NOT NULL")
        List<TodayRoutinesDto> getActivatedAndCompleteRoutines(@Param("member_entity") MemberEntity memberEntity);

        // 모든 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 기록 조회
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.TodayRoutinesDto(re.resultId, ro.routineId, ro.routineName, re.memberEntity.memberName, ro.dayOfWeek, ro.certification, ro.startTime, ro.endTime, re.createdAt, re.doneAt, false) FROM ResultEntity re JOIN re.routineEntity ro WHERE DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NULL")
        List<TodayRoutinesDto> getAllActivatedAndIncompleteRoutines();

        // 모든 사용자의 루틴 중 활성화되고 인증된 루틴 기록 조회
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.TodayRoutinesDto(re.resultId, ro.routineId, ro.routineName, re.memberEntity.memberName, ro.dayOfWeek, ro.certification, ro.startTime, ro.endTime, re.createdAt, re.doneAt, true) FROM ResultEntity re JOIN re.routineEntity ro WHERE DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NOT NULL")
        List<TodayRoutinesDto> getAllActivatedAndCompleteRoutines();

        // 이번 달 요일별 달성률
        @Query("SELECT CEIL(COALESCE((COUNT(CASE WHEN re.doneAt IS NOT NULL THEN 1 END) / COUNT(re.resultId) * 100), 0)) FROM ResultEntity re WHERE re.memberEntity = :member_entity AND YEAR(re.createdAt) = YEAR(CURRENT_DATE) AND MONTH(re.createdAt) = MONTH(CURRENT_DATE) AND FUNCTION('WEEKDAY', re.createdAt) = :day_of_week")
        Integer getDayOfWeekAchievement(
                        @Param("member_entity") MemberEntity memberEntity,
                        @Param("day_of_week") int dayOfWeek);

        // 이번 달 루틴별 달성률
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.RoutineAchievementDto(ro.routineName, CEIL(COALESCE((COUNT(CASE WHEN re.doneAt IS NOT NULL THEN 1 END) / COUNT(re.resultId) * 100), 0))) FROM ResultEntity re JOIN re.routineEntity ro WHERE re.memberEntity = :member_entity AND YEAR(re.createdAt) = YEAR(CURRENT_DATE) AND MONTH(re.createdAt) = MONTH(CURRENT_DATE) GROUP BY ro.routineName")
        List<RoutineAchievementDto> getRoutineAchievement(@Param("member_entity") MemberEntity memberEntity);

        // 사용자별 통계 정보 조회
        @Query("SELECT NEW com.miracle.miraclemorningback.dto.MemberStatisticsDto(re.memberEntity.memberId, re.memberEntity.memberName, COUNT(CASE WHEN re.doneAt IS NOT NULL THEN 1 END), COUNT(re.resultId)) FROM ResultEntity re GROUP BY re.memberEntity.memberId, re.memberEntity.memberName")
        List<MemberStatisticsDto> getMemberStatistics();
}