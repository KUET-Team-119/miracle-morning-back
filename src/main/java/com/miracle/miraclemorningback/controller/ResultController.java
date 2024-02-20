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

    // 기록 추가
    @PatchMapping("/api/results")
    public ResponseEntity<Object> updateResult(Authentication authentication,
            @RequestPart("data") ResultRequestDto requestDto,
            @RequestPart("file") MultipartFile file) throws IOException {

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

    // 오늘 날짜의 기록 조회
    @GetMapping("/api/result/today")
    public ResponseEntity<Object> getTodayResult() {
        return resultService.getTodayResult();
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

    // // 인증 사진 다운
    // @GetMapping("/api/result/proofFiles")
    // public List<ResultResponseDto> getProofFiles() throws IOException {
    // return resultService.getProofFiles();
    // }
}
