package com.miracle.miraclemorningback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.Role;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> { // <entity 및 pk 유형>

    Optional<MemberEntity> findByMemberName(String memberName);

    Boolean existsByMemberName(String memberName);

    // 회원 권한 수정
    // TODO member_id가 안되는 원인 찾기
    @Modifying
    @Query("UPDATE MemberEntity m SET m.role = :role WHERE m.memberId = :memberId")
    void updateMemberRole(@Param("memberId") Long memberId,
            @Param("role") Role role);
}