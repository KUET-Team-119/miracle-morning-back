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

        // 루틴 수정
        @Modifying
        @Query("UPDATE RoutineEntity ro SET ro.strategy = :strategy, ro.certification = :certification, ro.startTime = :start_time, ro.endTime = :end_time, ro.isActivated = :is_activated WHERE ro.routineId = :routine_id")
        void updateRoutine(@Param("routine_id") Long routineId,
                        @Param("strategy") String strategy,
                        @Param("certification") String certification,
                        @Param("start_time") Time startTime,
                        @Param("end_time") Time endTime,
                        @Param("is_activated") Boolean isActivated);

        // 활성화된 모든 루틴 가져오기
        @Query("SELECT ro FROM RoutineEntity ro WHERE ro.isActivated = true")
        List<RoutineEntity> getActivatedRoutines();

        // 특정 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
        @Query("SELECT ro FROM RoutineEntity ro JOIN ResultEntity re ON ro.routineName = re.routineName AND ro.memberName = re.memberName WHERE ro.memberName = :member_name AND DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NULL")
        List<RoutineEntity> getActivatedAndIncompleteRoutines(@Param("member_name") String memberName);

        // 특정 사용자의 루틴 중 활성화되고 인증된 루틴 조회
        @Query("SELECT ro FROM RoutineEntity ro JOIN ResultEntity re ON ro.routineName = re.routineName AND ro.memberName = re.memberName WHERE ro.memberName = :member_name AND DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NOT NULL")
        List<RoutineEntity> getActivatedAndCompleteRoutines(@Param("member_name") String memberName);

        // 모든 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
        @Query("SELECT ro FROM RoutineEntity ro JOIN ResultEntity re ON ro.routineName = re.routineName AND ro.memberName = re.memberName WHERE DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NULL")
        List<RoutineEntity> getAllActivatedAndIncompleteRoutines();

        // 모든 사용자의 루틴 중 활성화되고 인증된 루틴 조회
        @Query("SELECT ro FROM RoutineEntity ro JOIN ResultEntity re ON ro.routineName = re.routineName AND ro.memberName = re.memberName WHERE DATE(re.createdAt) = CURRENT_DATE AND re.doneAt IS NOT NULL")
        List<RoutineEntity> getAllActivatedAndCompleteRoutines();

        /*
         * 1차 배포에는 사용자 닉네임 변경 기능 제외
         * // 사용자의 닉네임이 변경되면 관련 루틴의 사용자명 변경
         * 
         * @Modifying
         * 
         * @Query("UPDATE RoutineEntity r SET r.memberName = :new_member_name WHERE r.memberName = :old_member_name"
         * )
         * void updateMemberName(@Param("old_member_name") String oldMemberName,
         * 
         * @Param("new_member_name") String newMemberName);
         */
}