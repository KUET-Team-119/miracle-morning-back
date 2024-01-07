package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.dto.RoutineDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.RoutineRequestDto;
import com.miracle.miraclemorningback.dto.RoutineResponseDto;
import com.miracle.miraclemorningback.entity.RoutineEntity;
import com.miracle.miraclemorningback.repository.RoutineRepository;

import lombok.RequiredArgsConstructor;

@Service // 스프링이 관리해주는 객체, 스프링 빈
@RequiredArgsConstructor // controller와 같이 final 멤버 변수 생성자를 만드는 역할
public class RoutineService {

    @Autowired
    private RoutineRepository routineRepository;

    // 전체 루틴 조회
    @Transactional(readOnly = true)
    public List<RoutineResponseDto> getRoutines() {
        return routineRepository.findAll().stream().map(RoutineResponseDto::new).toList();
    }

    // 루틴 추가
    @Transactional
    public RoutineResponseDto addRoutine(RoutineRequestDto requestDto) {
        RoutineEntity routineEntity = new RoutineEntity(requestDto);
        routineRepository.save(routineEntity);
        return new RoutineResponseDto(routineEntity);
    }

    // 특정 회원 루틴 검색
    @Transactional
    public RoutineResponseDto getRoutine(Long routine_id) {
        return routineRepository.findById(routine_id).map(RoutineResponseDto::new).orElseThrow(
                // 아이디가 존재하지 않으면 예외 처리
                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));
    }

    // 루틴 정보 수정
    @Transactional
    public RoutineResponseDto updateRoutine(Long routine_id, RoutineRequestDto requestDto) throws Exception {
        RoutineEntity routineEntity = routineRepository.findById(routine_id).orElseThrow(
                // 아이디가 존재하지 않으면 예외 처리
                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));

        routineEntity.update(requestDto);
        return new RoutineResponseDto(routineEntity);
    }

    // 루틴 삭제
    @Transactional
    public RoutineDeleteSuccessResponseDto deleteRoutine(Long routine_id)
            throws Exception {
        routineRepository.findById(routine_id).orElseThrow(
                // 아이디가 존재하지 않으면 예외 처리
                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));

        routineRepository.deleteById(routine_id);
        return new RoutineDeleteSuccessResponseDto(true);
    }

}
