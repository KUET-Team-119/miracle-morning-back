package com.miracle.miraclemorningback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.miraclemorningback.dto.RoutineRequestDto;
import com.miracle.miraclemorningback.entity.UserDetailsImpl;
import com.miracle.miraclemorningback.service.RoutineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    // 전체 루틴 조회
    @GetMapping("/api/routines")
    public ResponseEntity<Object> getRoutines() {
        return routineService.getRoutines();
    }

    // 루틴 추가
    @PostMapping("/api/routine")
    public ResponseEntity<Object> addRoutine(Authentication authentication, @RequestBody RoutineRequestDto requestDto) {
        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return routineService.addRoutine(memberName, requestDto);
    }

    // 특정 회원 루틴 검색
    @GetMapping("/api/routine")
    public ResponseEntity<Object> getRoutine(Authentication authentication) {
        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return routineService.getRoutine(memberName);
    }

    // 루틴 정보 수정
    @PatchMapping("/api/routine")
    public ResponseEntity<Object> updateRoutine(@RequestBody RoutineRequestDto requestDto) {
        return routineService.updateRoutine(requestDto);
    }

    // 루틴 삭제
    @DeleteMapping("/api/routine/{routineId}")
    public ResponseEntity<Object> deleteRoutine(@PathVariable Long routineId) {
        return routineService.deleteRoutine(routineId);
    }
}
