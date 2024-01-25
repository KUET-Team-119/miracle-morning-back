package com.miracle.miraclemorningback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.miracle.miraclemorningback.entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> { // 어떤 entity인지, pk 어떤 타입인지
    Optional<MemberEntity> findByMemberName(String memberName);

    Boolean existsByMemberName(String memberName);

    @Modifying
    @Query("UPDATE MemberEntity m set m.memberName = :new_member_name WHERE m.memberName = :old_member_name")
    void updateMemberName(@Param("old_member_name") String oldMemberName,
            @Param("new_member_name") String newMemberName);
}