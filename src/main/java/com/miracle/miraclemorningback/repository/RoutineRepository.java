package com.miracle.miraclemorningback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miracle.miraclemorningback.entity.RoutineEntity;

import java.sql.Time;
import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<RoutineEntity, Long> {
        List<RoutineEntity> findAllByMemberName(String memberName);

        @Modifying
        @Query("UPDATE RoutineEntity r set r.routineName = :routine_name, r.strategy = :strategy, r.certification = :certification, startTime = :start_time, endTime = :end_time, isActivated = :is_activated WHERE r.routineId = :routine_id")
        void updateSetting(@Param("routine_id") Long routineId, @Param("routine_name") String routineName,
                        @Param("strategy") String strategy,
                        @Param("certification") String certification, @Param("start_time") Time startTime,
                        @Param("end_time") Time endTime, @Param("is_activated") Boolean isActivated);

        @Modifying
        @Query("UPDATE RoutineEntity r set r.memberName = :new_member_name WHERE r.memberName = :old_member_name")
        void updateMemberName(@Param("old_member_name") String oldMemberName,
                        @Param("new_member_name") String newMemberName);

        // 특정 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
        @Query("SELECT ro FROM RoutineEntity ro WHERE ro.memberName = :member_name AND ro.isActivated = true AND ro.routineName NOT IN (SELECT re.routineName FROM ResultEntity re WHERE re.memberName = :member_name AND DATE(re.createdAt) = CURRENT_DATE)")
        List<RoutineEntity> getActivatedAndIncompleteRoutines(@Param("member_name") String memberName);

        // 특정 사용자의 루틴 중 활성화되고 인증된 루틴 조회
        @Query("SELECT ro FROM RoutineEntity ro WHERE ro.memberName = :member_name AND ro.isActivated = true AND ro.routineName IN (SELECT re.routineName FROM ResultEntity re WHERE re.memberName = :member_name AND DATE(re.createdAt) = CURRENT_DATE)")
        List<RoutineEntity> getActivatedAndCompleteRoutines(@Param("member_name") String memberName);

        // 모든 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
        @Query("SELECT ro FROM RoutineEntity ro WHERE ro.isActivated = true AND ro.routineName NOT IN (SELECT re.routineName FROM ResultEntity re WHERE DATE(re.createdAt) = CURRENT_DATE)")
        List<RoutineEntity> getAllActivatedAndIncompleteRoutines();

        // 모든 사용자의 루틴 중 활성화되고 인증된 루틴 조회
        @Query("SELECT ro FROM RoutineEntity ro WHERE ro.isActivated = true AND ro.routineName IN (SELECT re.routineName FROM ResultEntity re WHERE DATE(re.createdAt) = CURRENT_DATE)")
        List<RoutineEntity> getAllActivatedAndCompleteRoutines();
}