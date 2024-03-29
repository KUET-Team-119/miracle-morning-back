package com.miracle.miraclemorningback.service;

import java.util.List;

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
@RequiredArgsConstructor
public class RoutineService {

        private final RoutineRepository routineRepository;

        private final MemberRepository memberRepository;

        // 전체 루틴 조회
        @Transactional(readOnly = true)
        public ResponseEntity<Object> getRoutines() {
                List<RoutineResponseDto> routineResponseDto = routineRepository.findAll().stream()
                                .map(routineEntity -> RoutineResponseDto.builder()
                                                .routineId(routineEntity.getRoutineId())
                                                .routineName(routineEntity.getRoutineName())
                                                .memberName(routineEntity.getMemberEntity().getMemberName())
                                                .dayOfWeek(routineEntity.getDayOfWeek())
                                                .certification(routineEntity.getCertification())
                                                .startTime(routineEntity.getStartTime())
                                                .endTime(routineEntity.getEndTime())
                                                .isActivated(routineEntity.getIsActivated())
                                                .createdAt(routineEntity.getCreatedAt())
                                                .modifiedAt(routineEntity.getModifiedAt())
                                                .build())
                                .toList();

                return ResponseEntity.ok().body(routineResponseDto);
        }

        // 루틴 추가
        @Transactional
        public ResponseEntity<Object> addRoutine(String memberName, RoutineRequestDto requestDto) {

                MemberEntity memberEntity = memberRepository.findByMemberName(memberName).orElse(null);

                if (memberEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                if (routineRepository.existsByRoutineNameAndMemberEntity(requestDto.getRoutineName(), memberEntity)) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("이미 존재하는 리소스입니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(requestSuccessDto);
                } else {
                        // 존재하지 않은 사용자, 중복되지 않은 루틴명인 경우 -> 루틴 추가 로직 수행
                        RoutineEntity routineEntity = RoutineEntity.builder()
                                        .routineName(requestDto.getRoutineName())
                                        .dayOfWeek(requestDto.getDayOfWeek())
                                        .certification(requestDto.getCertification())
                                        .startTime(requestDto.getStartTime())
                                        .endTime(requestDto.getEndTime())
                                        .isActivated(requestDto.getIsActivated())
                                        .build();

                        routineEntity.setMemberEntity(memberEntity);

                        routineRepository.save(routineEntity);

                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(true)
                                        .message("요청이 성공적으로 처리되었습니다.")
                                        .build();

                        return ResponseEntity.ok().body(requestSuccessDto);
                }
        }

        // 특정 회원 루틴 검색
        @Transactional
        public ResponseEntity<Object> getRoutine(String memberName) {

                MemberEntity memberEntity = memberRepository.findByMemberName(memberName).orElse(null);

                // 사용자가 존재하지 않으면 UNAUTHORIZED 상태 코드를 반환
                if (memberEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                List<RoutineResponseDto> routineResponseDto = routineRepository
                                .findAllByMemberEntity(memberEntity).stream()
                                .map(routineEntity -> RoutineResponseDto.builder()
                                                .routineId(routineEntity.getRoutineId())
                                                .routineName(routineEntity.getRoutineName())
                                                .memberName(routineEntity.getMemberEntity().getMemberName())
                                                .dayOfWeek(routineEntity.getDayOfWeek())
                                                .certification(routineEntity.getCertification())
                                                .startTime(routineEntity.getStartTime())
                                                .endTime(routineEntity.getEndTime())
                                                .isActivated(routineEntity.getIsActivated())
                                                .createdAt(routineEntity.getCreatedAt())
                                                .modifiedAt(routineEntity.getModifiedAt())
                                                .build())
                                .toList();

                return ResponseEntity.ok().body(routineResponseDto);
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
                                requestDto.getDayOfWeek(),
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
}
