package com.miracle.miraclemorningback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miracle.miraclemorningback.entity.RoutineEntity;

@Repository
public interface RoutineRepository extends JpaRepository<RoutineEntity, Long> {
}