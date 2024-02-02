package com.miracle.miraclemorningback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @PutMapping("/api/routine")
    public RoutineResponseDto updateRoutine(@RequestBody RoutineRequestDto requestDto)
            throws Exception {
        return routineService.updateRoutine(requestDto);
    }

    // 루틴 삭제
    @DeleteMapping("/api/routine/{routineId}")
    public RoutineDeleteSuccessResponseDto deleteRoutine(@PathVariable Long routineId) throws Exception {
        return routineService.deleteRoutine(routineId);
    }

    /*
     * 1차 배포에는 사용자 닉네임 변경 기능 미적용
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

    /*
     * 아래 두 api는 오늘 날짜의 기록만 조회 api로 기능 통합
     * 
     * // 특정 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
     * 
     * @GetMapping("/api/routines/rest/{memberName}")
     * public List<RoutineResponseDto>
     * getActivatedAndUnfinishedRoutines(@PathVariable String memberName) {
     * return routineService.getActivatedAndIncompleteRoutines(memberName);
     * }
     * 
     * // 특정 사용자의 루틴 중 활성화되고 인증된 루틴 조회
     * 
     * @GetMapping("/api/routines/clear/{memberName}")
     * public List<RoutineResponseDto> getActivatedAndFinishedRoutines(@PathVariable
     * String memberName) {
     * return routineService.getActivatedAndCompleteRoutines(memberName);
     * }
     */

    // 특정 사용자의 오늘 날짜의 기록만 조회
    @GetMapping("/api/routines/today")
    public List<TodayRoutinesDto> getTodayRoutines(Authentication authentication) {
        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return routineService.getTodayRoutines(memberName);
    }

    // 모든 사용자의 오늘 날짜의 기록만 조회
    @GetMapping("/api/all/routines/today")
    public List<TodayRoutinesDto> getAllTodayRoutines() {
        return routineService.getAllTodayRoutines();
    }
}
