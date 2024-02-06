package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.dto.RoutineDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.RoutineRequestDto;
import com.miracle.miraclemorningback.dto.RoutineResponseDto;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.RoutineEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;
import com.miracle.miraclemorningback.repository.RoutineRepository;

import lombok.RequiredArgsConstructor;

@Service // 스프링이 관리해주는 객체, 스프링 빈
@RequiredArgsConstructor // controller와 같이 final 멤버 변수 생성자를 만드는 역할
public class RoutineService {

        @Autowired
        private RoutineRepository routineRepository;

        @Autowired
        private MemberRepository memberRepository;

        // 전체 루틴 조회
        @Transactional(readOnly = true)
        public List<RoutineResponseDto> getRoutines() {
                return routineRepository.findAll().stream()
                                .map(routineEntity -> RoutineResponseDto.builder()
                                                .routineId(routineEntity.getRoutineId())
                                                .routineName(routineEntity.getRoutineName())
                                                .memberName(routineEntity.getMemberEntity().getMemberName())
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

        // 루틴 추가
        @Transactional
        public RoutineResponseDto addRoutine(String memberName, RoutineRequestDto requestDto) {
                RoutineEntity routineEntity = RoutineEntity.builder()
                                .routineName(requestDto.getRoutineName())
                                .strategy(requestDto.getStrategy())
                                .certification(requestDto.getCertification())
                                .startTime(requestDto.getStartTime())
                                .endTime(requestDto.getEndTime())
                                .isActivated(requestDto.getIsActivated())
                                .build();

                routineEntity.setMemberEntity(memberRepository.findByMemberName(memberName).get());

                routineRepository.save(routineEntity);

                return RoutineResponseDto.builder()
                                .routineId(routineEntity.getRoutineId())
                                .routineName(routineEntity.getRoutineName())
                                .memberName(routineEntity.getMemberEntity().getMemberName())
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
                MemberEntity memberEntity = memberRepository.findByMemberName(memberName).get();
                return routineRepository.findAllByMemberEntity(memberEntity).stream()
                                .map(routineEntity -> RoutineResponseDto.builder()
                                                .routineId(routineEntity.getRoutineId())
                                                .routineName(routineEntity.getRoutineName())
                                                .memberName(routineEntity.getMemberEntity().getMemberName())
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
        public RoutineResponseDto updateRoutine(RoutineRequestDto requestDto) throws Exception {
                RoutineEntity routineEntity = routineRepository.findById(requestDto.getRoutineId()).orElseThrow(
                                // 아이디가 존재하지 않으면 예외 처리
                                () -> new IllegalArgumentException("존재하지 않은 루틴입니다."));

                routineRepository.updateRoutine(
                                requestDto.getRoutineId(),
                                requestDto.getStrategy(),
                                requestDto.getCertification(),
                                requestDto.getStartTime(),
                                requestDto.getEndTime(),
                                requestDto.getIsActivated());

                return RoutineResponseDto.builder()
                                .routineId(routineEntity.getRoutineId())
                                .routineName(routineEntity.getRoutineName())
                                .memberName(routineEntity.getMemberEntity().getMemberName())
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

        /*
         * 1차 배포에는 사용자 닉네임 변경 기능 제외
         * // 루틴의 사용자 닉네임 변경
         * 
         * @Transactional
         * public void updateMemberName(String memberName, RoutineRequestDto requestDto)
         * throws Exception {
         * routineRepository.updateMemberName(memberName, requestDto.getMemberName());
         * }
         */
}
