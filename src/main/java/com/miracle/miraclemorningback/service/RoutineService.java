package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.dto.RequestSuccessDto;
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
        public ResponseEntity<Object> addRoutine(String memberName, RoutineRequestDto requestDto) {
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

                RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                .success(true)
                                .message("요청이 성공적으로 처리되었습니다.")
                                .build();

                return ResponseEntity.ok().body(requestSuccessDto);
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
        public ResponseEntity<Object> updateRoutine(RoutineRequestDto requestDto) {

                if (!routineRepository.existsById(requestDto.getRoutineId())) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스를 찾을 수 없습니다.")
                                        .build();

                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                routineRepository.updateRoutine(
                                requestDto.getRoutineId(),
                                requestDto.getStrategy(),
                                requestDto.getCertification(),
                                requestDto.getStartTime(),
                                requestDto.getEndTime(),
                                requestDto.getIsActivated());

                RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                .success(true)
                                .message("요청이 성공적으로 처리되었습니다.")
                                .build();

                return ResponseEntity.ok().body(requestSuccessDto);
        }

        // 루틴 삭제
        @Transactional
        public ResponseEntity<Object> deleteRoutine(Long routineId) {
                if (!routineRepository.existsById(routineId)) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스를 찾을 수 없습니다.")
                                        .build();

                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                } else {
                        routineRepository.deleteById(routineId);
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(true)
                                        .message("요청이 성공적으로 처리되었습니다.")
                                        .build();

                        return ResponseEntity.ok().body(requestSuccessDto);
                }
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
