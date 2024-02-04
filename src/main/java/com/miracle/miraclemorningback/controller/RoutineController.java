package com.miracle.miraclemorningback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.miraclemorningback.dto.RoutineDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.RoutineRequestDto;
import com.miracle.miraclemorningback.dto.RoutineResponseDto;
import com.miracle.miraclemorningback.dto.TodayRoutinesDto;
import com.miracle.miraclemorningback.entity.UserDetailsImpl;
import com.miracle.miraclemorningback.service.RoutineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RoutineController {

    @Autowired
    private RoutineService routineService;

    // 전체 루틴 조회
    @GetMapping("/api/routines")
    public List<RoutineResponseDto> getRoutines() {
        return routineService.getRoutines();
    }

    // 루틴 추가
    @PostMapping("/api/routine")
    public RoutineResponseDto addRoutine(Authentication authentication, @RequestBody RoutineRequestDto requestDto) {
        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return routineService.addRoutine(memberName, requestDto);
    }

    // 특정 회원 루틴 검색
    @GetMapping("/api/routine")
    public List<RoutineResponseDto> getRoutine(Authentication authentication) {
        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return routineService.getRoutine(memberName);
    }

    // 루틴 정보 수정
    @PatchMapping("/api/routine")
    public RoutineResponseDto updateRoutine(@RequestBody RoutineRequestDto requestDto)
            throws Exception {
        return routineService.updateRoutine(requestDto);
    }

    // 루틴 삭제
    @DeleteMapping("/api/routine/{routineId}")
    public RoutineDeleteSuccessResponseDto deleteRoutine(@PathVariable Long routineId) throws Exception {
        return routineService.deleteRoutine(routineId);
    }

    // 특정 사용자의 오늘 날짜의 기록만 조회
    @GetMapping("/api/routines/today")
    public List<TodayRoutinesDto> getTodayRoutines(Authentication authentication) {
        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return routineService.getTodayRoutines(memberName);
    }

    // 모든 사용자의 오늘 날짜의 루틴 완료 여부 조회
    @GetMapping("/api/all/routines/today")
    public List<TodayRoutinesDto> getAllTodayRoutines(Authentication authentication) {
        return routineService.getAllTodayRoutines();
    }

    /*
     * 1차 배포에는 사용자 닉네임 변경 기능 제외
     * // 루틴의 사용자 닉네임 변경
     * 
     * @PutMapping("/api/routine/nickname/{memberName}")
     * public void updateNickname(@PathVariable String memberName,
     * 
     * @RequestBody RoutineRequestDto requestDto)
     * throws Exception {
     * routineService.updateMemberName(memberName, requestDto);
     * }
     */
}
