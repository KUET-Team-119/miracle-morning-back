package com.miracle.miraclemorningback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miracle.miraclemorningback.entity.ComplaintEntity;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity, Long> {
}
