package com.miracle.miraclemorningback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.RoutineEntity;

import java.sql.Time;
import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<RoutineEntity, Long> {
        List<RoutineEntity> findAllByMemberEntity(MemberEntity memberEntity);

        Boolean existsByRoutineNameAndMemberEntity(String routineName, MemberEntity memberEntity);

        // 루틴 수정
        @Modifying
        @Query("UPDATE RoutineEntity ro SET ro.dayOfWeek = :day_of_week, ro.certification = :certification, ro.startTime = :start_time, ro.endTime = :end_time, ro.isActivated = :is_activated WHERE ro.routineId = :routine_id")
        void updateRoutine(@Param("routine_id") Long routineId,
                        @Param("day_of_week") String dayOfWeek,
                        @Param("certification") String certification,
                        @Param("start_time") Time startTime,
                        @Param("end_time") Time endTime,
                        @Param("is_activated") Boolean isActivated);

        // 활성화되고 실천 요일이 유효한 모든 루틴 가져오기
        @Query("SELECT ro FROM RoutineEntity ro WHERE ro.isActivated = true AND SUBSTRING(ro.dayOfWeek, CAST(FUNCTION('WEEKDAY', CURRENT_DATE) AS int) + 1, 1) = '1'")
        List<RoutineEntity> getActivatedAndValidDayOfWeekRoutines();

}