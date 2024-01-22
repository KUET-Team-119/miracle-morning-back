package com.miracle.miraclemorningback.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.dto.RoutineDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.RoutineRequestDto;
import com.miracle.miraclemorningback.dto.RoutineResponseDto;
import com.miracle.miraclemorningback.dto.TodayRoutinesDto;
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
                return routineRepository.findAll().stream()
                                .map(routineEntity -> RoutineResponseDto.builder()
                                                .routineId(routineEntity.getRoutineId())
                                                .routineName(routineEntity.getRoutineName())
                                                .memberName(routineEntity.getMemberName())
                                                .strategy(routineEntity.getStrategy())
                                                .certification(routineEntity.getCertification())
                                                .startTime(routineEntity.getStartTime())
                                                .endTime(routineEntity.getEndTime())
                                                .isActivated(routineEntity.getIsActivated())
                                                .createdAt(routineEntity.getCreatedAt())
                                                .modifiedAt(routineEntity.getModifiedAt())
                                                .build())
                                .toList();

                // 빌더 패턴 이전
                // return
                // routineRepository.findAll().stream().map(RoutineResponseDto::new).toList();
        }

        // 루틴 추가
        @Transactional
        public RoutineResponseDto addRoutine(RoutineRequestDto requestDto) {
                RoutineEntity routineEntity = RoutineEntity.builder()
                                .routineName(requestDto.getRoutineName())
                                .memberName(requestDto.getMemberName())
                                .strategy(requestDto.getStrategy())
                                .certification(requestDto.getCertification())
                                .startTime(requestDto.getStartTime())
                                .endTime(requestDto.getEndTime())
                                .isActivated(requestDto.getIsActivated())
                                .build();
                routineRepository.save(routineEntity);

                return RoutineResponseDto.builder()
                                .routineId(routineEntity.getRoutineId())
                                .routineName(routineEntity.getRoutineName())
                                .memberName(routineEntity.getMemberName())
                                .strategy(routineEntity.getStrategy())
                                .certification(routineEntity.getCertification())
                                .startTime(routineEntity.getStartTime())
                                .endTime(routineEntity.getEndTime())
                                .isActivated(routineEntity.getIsActivated())
                                .createdAt(routineEntity.getCreatedAt())
                                .modifiedAt(routineEntity.getModifiedAt())
                                .build();
        }

        // 특정 회원 루틴 검색
        @Transactional
        public List<RoutineResponseDto> getRoutine(String memberName) {
                return routineRepository.findAllByMemberName(memberName).stream()
                                .map(routineEntity -> RoutineResponseDto.builder()
                                                .routineId(routineEntity.getRoutineId())
                                                .routineName(routineEntity.getRoutineName())
                                                .memberName(routineEntity.getMemberName())
                                                .strategy(routineEntity.getStrategy())
                                                .certification(routineEntity.getCertification())
                                                .startTime(routineEntity.getStartTime())
                                                .endTime(routineEntity.getEndTime())
                                                .isActivated(routineEntity.getIsActivated())
                                                .createdAt(routineEntity.getCreatedAt())
                                                .modifiedAt(routineEntity.getModifiedAt())
                                                .build())
                                .toList();
        }

        // 루틴 정보 수정
        @Transactional
        public RoutineResponseDto updateRoutine(Long routineId, RoutineRequestDto requestDto) throws Exception {
                RoutineEntity routineEntity = routineRepository.findById(routineId).orElseThrow(
                                // 아이디가 존재하지 않으면 예외 처리
                                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));

                routineRepository.updateSetting(routineId, requestDto.getRoutineName(), requestDto.getStrategy(),
                                requestDto.getCertification(), requestDto.getStartTime(), requestDto.getEndTime(),
                                requestDto.getIsActivated());

                return RoutineResponseDto.builder()
                                .routineId(routineEntity.getRoutineId())
                                .routineName(routineEntity.getRoutineName())
                                .memberName(routineEntity.getMemberName())
                                .strategy(routineEntity.getStrategy())
                                .certification(routineEntity.getCertification())
                                .startTime(routineEntity.getStartTime())
                                .endTime(routineEntity.getEndTime())
                                .isActivated(routineEntity.getIsActivated())
                                .createdAt(routineEntity.getCreatedAt())
                                .modifiedAt(routineEntity.getModifiedAt())
                                .build();
        }

        // 루틴 삭제
        @Transactional
        public RoutineDeleteSuccessResponseDto deleteRoutine(Long routineId)
                        throws Exception {
                routineRepository.findById(routineId).orElseThrow(
                                // 아이디가 존재하지 않으면 예외 처리
                                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));

                routineRepository.deleteById(routineId);
                return RoutineDeleteSuccessResponseDto.builder().success(true).build();
        }

        // 루틴의 사용자 닉네임 변경
        @Transactional
        public void updateMemberName(String memberName, RoutineRequestDto requestDto) throws Exception {
                routineRepository.updateMemberName(memberName, requestDto.getMemberName());
        }

        // 특정 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
        @Transactional
        public List<RoutineResponseDto> getActivatedAndIncompleteRoutines(String memberName) {
                return routineRepository.getActivatedAndIncompleteRoutines(memberName).stream()
                                .map(routineEntity -> RoutineResponseDto.builder()
                                                .routineId(routineEntity.getRoutineId())
                                                .routineName(routineEntity.getRoutineName())
                                                .memberName(routineEntity.getMemberName())
                                                .strategy(routineEntity.getStrategy())
                                                .certification(routineEntity.getCertification())
                                                .startTime(routineEntity.getStartTime())
                                                .endTime(routineEntity.getEndTime())
                                                .isActivated(routineEntity.getIsActivated())
                                                .createdAt(routineEntity.getCreatedAt())
                                                .modifiedAt(routineEntity.getModifiedAt())
                                                .build())
                                .toList();
        }

        // 특정 사용자의 루틴 중 활성화되고 인증된 루틴 조회
        @Transactional
        public List<RoutineResponseDto> getActivatedAndCompleteRoutines(String memberName) {
                return routineRepository.getActivatedAndCompleteRoutines(memberName).stream()
                                .map(routineEntity -> RoutineResponseDto.builder()
                                                .routineId(routineEntity.getRoutineId())
                                                .routineName(routineEntity.getRoutineName())
                                                .memberName(routineEntity.getMemberName())
                                                .strategy(routineEntity.getStrategy())
                                                .certification(routineEntity.getCertification())
                                                .startTime(routineEntity.getStartTime())
                                                .endTime(routineEntity.getEndTime())
                                                .isActivated(routineEntity.getIsActivated())
                                                .createdAt(routineEntity.getCreatedAt())
                                                .modifiedAt(routineEntity.getModifiedAt())
                                                .build())
                                .toList();
        }

        // 오늘 날짜의 기록만 조회
        @Transactional
        public List<TodayRoutinesDto> getTodayRoutines(String memberName) {

                List<TodayRoutinesDto> todayRoutinesDto = new ArrayList<>();

                List<TodayRoutinesDto> incompleteRoutines = new ArrayList<>();
                List<TodayRoutinesDto> completeRoutines = new ArrayList<>();

                // 특정 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
                incompleteRoutines = routineRepository.getActivatedAndIncompleteRoutines(memberName).stream()
                                .map(routineEntity -> TodayRoutinesDto.builder()
                                                .routineId(routineEntity.getRoutineId())
                                                .routineName(routineEntity.getRoutineName())
                                                .memberName(routineEntity.getMemberName())
                                                .strategy(routineEntity.getStrategy())
                                                .certification(routineEntity.getCertification())
                                                .startTime(routineEntity.getStartTime())
                                                .endTime(routineEntity.getEndTime())
                                                .createdAt(routineEntity.getCreatedAt())
                                                .complete(false)
                                                .build())
                                .toList();

                // 특정 사용자의 루틴 중 활성화되고 인증된 루틴 조회
                completeRoutines = routineRepository.getActivatedAndCompleteRoutines(memberName).stream()
                                .map(routineEntity -> TodayRoutinesDto.builder()
                                                .routineId(routineEntity.getRoutineId())
                                                .routineName(routineEntity.getRoutineName())
                                                .memberName(routineEntity.getMemberName())
                                                .strategy(routineEntity.getStrategy())
                                                .certification(routineEntity.getCertification())
                                                .startTime(routineEntity.getStartTime())
                                                .endTime(routineEntity.getEndTime())
                                                .createdAt(routineEntity.getCreatedAt())
                                                .complete(true)
                                                .build())
                                .toList();

                todayRoutinesDto.addAll(incompleteRoutines);
                todayRoutinesDto.addAll(completeRoutines);

                return todayRoutinesDto;
        }
}
