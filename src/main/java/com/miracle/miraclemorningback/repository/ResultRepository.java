package com.miracle.miraclemorningback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.miracle.miraclemorningback.entity.ResultEntity;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, Long> {

    // 오늘 날짜의 기록만 조회
    @Query("SELECT re FROM ResultEntity re WHERE DATE(re.createdAt) = CURRENT_DATE")
    List<ResultEntity> findAllByCurrentDate();
}