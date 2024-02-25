package com.miracle.miraclemorningback.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.miracle.miraclemorningback.dto.ResultRequestDto;
import com.miracle.miraclemorningback.entity.UserDetailsImpl;
import com.miracle.miraclemorningback.service.ResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    // 전체 기록 조회
    @GetMapping("/api/all/results")
    public ResponseEntity<Object> getResults() {
        return resultService.getResults();
    }

    // 사용자가 인증한 기록 중 오늘 날짜의 기록 조회
    @GetMapping("/api/results/today")
    public ResponseEntity<Object> getCompleteAndTodayResults() {
        return resultService.getCompleteAndTodayResults();
    }

    // 사용자가 인증한 기록 중 오늘을 포함해서 4일 전까지의 기록 조회
    @GetMapping("/api/admin/results/recent")
    public ResponseEntity<Object> getCompleteAndRecentResults() {
        return resultService.getCompleteAndRecentResults();
    }

    // 기록 추가
    @PatchMapping("/api/results")
    public ResponseEntity<Object> updateResult(Authentication authentication,
            @RequestPart("data") ResultRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();

        return resultService.updateResult(memberName, requestDto, file);
    }

    // 특정 사용자의 특정 기간 기록 검색
    @GetMapping("/api/results")
    public ResponseEntity<Object> getResultsByDate(
            @RequestParam("member-id") Long memberId,
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month) {
        return resultService.getResultsByDate(memberId, year, month);
    }

    // 기록 삭제
    @DeleteMapping("/api/result/{resultId}")
    public ResponseEntity<Object> deleteResult(@PathVariable Long resultId) {
        return resultService.deleteResult(resultId);
    }

    // 특정 사용자의 오늘 날짜의 기록 조회
    @GetMapping("/api/routines/today")
    public ResponseEntity<Object> getTodayRoutines(Authentication authentication) {
        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return resultService.getTodayRoutines(memberName);
    }

    // 모든 사용자의 오늘 날짜의 루틴 완료 여부 조회
    @GetMapping("/api/all/routines/today")
    public ResponseEntity<Object> getAllTodayRoutines(Authentication authentication) {
        return resultService.getAllTodayRoutines();
    }

    // 이번 달 요일별 달성률
    @GetMapping("/api/results/week")
    public ResponseEntity<Object> getDayOfWeekAchievement(Authentication authentication) {
        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return resultService.getDayOfWeekAchievement(memberName);
    }

    // 이번 달 루틴별 달성률
    @GetMapping("api/results/routines")
    public ResponseEntity<Object> getRoutineAchievement(Authentication authentication) {
        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return resultService.getRoutineAchievement(memberName);
    }

    // 사용자별 통계 정보 조회
    @GetMapping("api/admin/members/statistics")
    public ResponseEntity<Object> getMemberStatistics() {
        return resultService.getMemberStatistics();
    }
}
