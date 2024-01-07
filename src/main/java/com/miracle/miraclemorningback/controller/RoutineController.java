package com.miracle.miraclemorningback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public RoutineResponseDto addRoutine(@RequestBody RoutineRequestDto requestDto) {
        return routineService.addRoutine(requestDto);
    }

    // 특정 회원 루틴 검색
    @GetMapping("/api/routine/{routine_id}")
    public RoutineResponseDto getRoutine(@PathVariable Long routine_id) {
        return routineService.getRoutine(routine_id);
    }

    // 루틴 정보 수정
    @PutMapping("/api/routine/{routine_id}")
    public RoutineResponseDto updateRoutine(@PathVariable Long routine_id, @RequestBody RoutineRequestDto requestDto)
            throws Exception {
        return routineService.updateRoutine(routine_id, requestDto);
    }

    // 회원 삭제
    @DeleteMapping("/api/routine/{routine_id}")
    public RoutineDeleteSuccessResponseDto deleteRoutine(@PathVariable Long routine_id) throws Exception {
        return routineService.deleteRoutine(routine_id);
    }
}
