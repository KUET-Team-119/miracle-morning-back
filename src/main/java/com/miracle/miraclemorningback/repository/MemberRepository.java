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
    Optional<MemberEntity> findByNickname(String nickname);

    @Modifying
    @Query("UPDATE MemberEntity m set m.nickname = :new_nickname WHERE m.nickname = :old_nickname")
    void updateNickname(@Param("old_nickname") String oldNickname, @Param("new_nickname") String newNickname);
}