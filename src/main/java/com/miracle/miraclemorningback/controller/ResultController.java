package com.miracle.miraclemorningback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.miraclemorningback.dto.ResultDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.ResultRequestDto;
import com.miracle.miraclemorningback.dto.ResultResponseDto;
import com.miracle.miraclemorningback.service.ResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ResultController {

    @Autowired
    private ResultService resultService;

    // 전체 기록 조회
    @GetMapping("/api/results")
    public List<ResultResponseDto> getResults() {
        return resultService.getResults();
    }

    // 기록 추가
    @PostMapping("/api/result")
    public ResultResponseDto addResult(@RequestBody ResultRequestDto requestDto) {
        return resultService.addResult(requestDto);
    }

    // 특정 기간 기록 검색
    // To-do resultId에서 날짜 데이터로 변경
    @GetMapping("/api/result/{date}")
    public ResultResponseDto getResult(@PathVariable Long resultId) {
        return resultService.getResult(resultId);
    }

    // 기록 삭제
    @DeleteMapping("/api/result/{resultId}")
    public ResultDeleteSuccessResponseDto deleteResult(@PathVariable Long resultId) throws Exception {
        return resultService.deleteResult(resultId);
    }

    // 오늘 날짜의 기록만 조회
    @GetMapping("/api/result/today")
    public List<ResultResponseDto> getTodayResult() {
        return resultService.getTodayResult();
    }
}
