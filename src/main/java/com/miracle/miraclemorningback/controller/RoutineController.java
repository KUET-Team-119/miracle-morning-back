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
    @GetMapping("/routines")
    public List<RoutineResponseDto> getRoutines() {
        return routineService.getRoutines();
    }

    // 루틴 추가
    @PostMapping("/routines/add")
    public RoutineResponseDto addRoutine(@RequestBody RoutineRequestDto requestDto) {
        return routineService.addRoutine(requestDto);
    }

    // 특정 회원 루틴 검색
    @GetMapping("/routines/{memberName}")
    public List<RoutineResponseDto> getRoutine(@PathVariable String memberName) {
        return routineService.getRoutine(memberName);
    }

    // 루틴 정보 수정
    @PutMapping("/routines/update/{routineId}")
    public RoutineResponseDto updateRoutine(@PathVariable Long routineId, @RequestBody RoutineRequestDto requestDto)
            throws Exception {
        return routineService.updateRoutine(routineId, requestDto);
    }

    // 회원 삭제
    @DeleteMapping("/routines/delete/{routineId}")
    public RoutineDeleteSuccessResponseDto deleteRoutine(@PathVariable Long routineId) throws Exception {
        return routineService.deleteRoutine(routineId);
    }

    // 루틴의 사용자 닉네임 변경
    @PutMapping("/routines/nickname/{memberName}")
    public void updateNickname(@PathVariable String memberName,
            @RequestBody RoutineRequestDto requestDto)
            throws Exception {
        routineService.updateMemberName(memberName, requestDto);
    }
}