package com.miracle.miraclemorningback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.miracle.miraclemorningback.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

}