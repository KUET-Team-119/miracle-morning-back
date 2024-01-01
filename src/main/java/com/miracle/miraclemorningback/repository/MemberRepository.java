package com.miracle.miraclemorningback.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.miracle.miraclemorningback.entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> { // 어떤 entity인지, pk 어떤 타입인지
    // 닉네임으로 회원 정보 조회(SELECT * from member_table where member_nickname=?)
    Optional<MemberEntity> findByNickname(String Nickname);
}
