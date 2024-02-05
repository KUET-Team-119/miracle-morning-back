package com.miracle.miraclemorningback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.miracle.miraclemorningback.entity.TodayRoutinesEntity;

public interface TodayRoutinesRepository extends JpaRepository<TodayRoutinesEntity, Long> {
    // 특정 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
    @Query(value = "SELECT ro.routine_id, ro.routine_name, ro.member_name, ro.strategy, ro.certification, ro.start_time, ro.end_time, re.created_at, re.done_at FROM routine ro JOIN result re ON ro.routine_name = re.routine_name AND ro.member_name = re.member_name WHERE ro.member_name = :member_name AND DATE(re.created_at) = CURRENT_DATE AND re.done_at IS NULL", nativeQuery = true)
    List<TodayRoutinesEntity> getActivatedAndIncompleteRoutines(@Param("member_name") String memberName);

    // 특정 사용자의 루틴 중 활성화되고 인증된 루틴 조회
    @Query(value = "SELECT ro.routine_id, ro.routine_name, ro.member_name, ro.strategy, ro.certification, ro.start_time, ro.end_time, re.created_at, re.done_at FROM routine ro JOIN result re ON ro.routine_name = re.routine_name AND ro.member_name = re.member_name WHERE ro.member_name = :member_name AND DATE(re.created_at) = CURRENT_DATE AND re.done_at IS NOT NULL", nativeQuery = true)
    List<TodayRoutinesEntity> getActivatedAndCompleteRoutines(@Param("member_name") String memberName);

    // 모든 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
    @Query(value = "SELECT ro.routine_id, ro.routine_name, ro.member_name, ro.strategy, ro.certification, ro.start_time, ro.end_time, re.created_at, re.done_at FROM routine ro JOIN result re ON ro.routine_name = re.routine_name AND ro.member_name = re.member_name WHERE DATE(re.created_at) = CURRENT_DATE AND re.done_at IS NULL", nativeQuery = true)
    List<TodayRoutinesEntity> getAllActivatedAndIncompleteRoutines();

    // 모든 사용자의 루틴 중 활성화되고 인증된 루틴 조회
    @Query(value = "SELECT ro.routine_id, ro.routine_name, ro.member_name, ro.strategy, ro.certification, ro.start_time, ro.end_time, re.created_at, re.done_at FROM routine ro JOIN result re ON ro.routine_name = re.routine_name AND ro.member_name = re.member_name WHERE DATE(re.created_at) = CURRENT_DATE AND re.done_at IS NOT NULL", nativeQuery = true)
    List<TodayRoutinesEntity> getAllActivatedAndCompleteRoutines();
}
