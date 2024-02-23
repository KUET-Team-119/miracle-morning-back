package com.miracle.miraclemorningback.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.miracle.miraclemorningback.dto.DayOfWeekAchievementDto;
import com.miracle.miraclemorningback.dto.RequestSuccessDto;
import com.miracle.miraclemorningback.dto.ResultRequestDto;
import com.miracle.miraclemorningback.dto.ResultResponseDto;
import com.miracle.miraclemorningback.dto.RoutineAchievementDto;
import com.miracle.miraclemorningback.dto.TodayRoutinesDto;
import com.miracle.miraclemorningback.entity.DayOfWeek;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.ResultEntity;
import com.miracle.miraclemorningback.entity.RoutineEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;
import com.miracle.miraclemorningback.repository.ResultRepository;
import com.miracle.miraclemorningback.repository.RoutineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultService {

        private final MemberRepository memberRepository;

        private final RoutineRepository routineRepository;

        private final ResultRepository resultRepository;

        // 인증 사진 저장 경로
        @Value("${images.path}")
        private String DIR_PATH;

        // 전체 기록 조회
        @Transactional(readOnly = true)
        public ResponseEntity<Object> getResults() {
                List<ResultResponseDto> listOfResults = resultRepository.findAll().stream()
                                .map(resultEntity -> ResultResponseDto.builder()
                                                .resultId(resultEntity.getResultId())
                                                .routineName(resultEntity.getRoutineEntity().getRoutineName())
                                                .memberName(resultEntity.getMemberEntity().getMemberName())
                                                .doneAt(resultEntity.getDoneAt())
                                                .createdAt(resultEntity.getCreatedAt())
                                                .build())
                                .toList();

                return ResponseEntity.ok().body(listOfResults);
        }

        // 루틴 인증을 위한 기록 추가
        @Transactional
        public ResponseEntity<Object> updateResult(String memberName, ResultRequestDto requestDto, MultipartFile file)
                        throws IOException {

                MemberEntity memberEntity = memberRepository.findByMemberName(memberName).orElseGet(null);

                if (memberEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                RoutineEntity routineEntity = routineRepository.findById(requestDto.getRoutineId()).orElseGet(null);

                if (routineEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                ResultEntity resultEntity = resultRepository.findByRoutineEntityAndMemberEntityAndCurrentDate(
                                routineEntity, memberEntity).orElse(null);

                // 기록이 존재하지 않으면 NOT_FOUND 상태 코드를 반환
                if (resultEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                // 파일 업로드 시간
                LocalDateTime dateTime = LocalDateTime.now();
                String timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateTime);

                // 파일명: 닉네임 + 루틴명 + 업로드 시간 + 원본 파일명
                String fileName = memberName + "_" + requestDto.getRoutineId() + "_" + timeStamp
                                + "_" + file.getOriginalFilename();

                // 파일 경로
                String filePath = DIR_PATH + fileName;
                file.transferTo(new File(DIR_PATH, fileName));

                // 기록 업데이트
                resultRepository.updateResult(
                                routineEntity,
                                memberEntity,
                                requestDto.getDoneAt(),
                                filePath);

                RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                .success(true)
                                .message("요청을 성공적으로 처리했습니다.")
                                .build();

                return ResponseEntity.ok().body(requestSuccessDto);
        }

        // 특정 사용자의 특정 기간 기록 검색
        @Transactional
        public ResponseEntity<Object> getResultsByDate(Long memberId, Integer year, Integer month) {

                MemberEntity memberEntity = memberRepository.findById(memberId).orElseGet(null);

                if (memberEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                List<ResultResponseDto> listOfResultResponseDto = resultRepository
                                .findAllByIdAndYearAndMonth(memberEntity, year, month).stream()
                                .map(resultEntity -> ResultResponseDto.builder()
                                                .resultId(resultEntity.getResultId())
                                                .routineId(resultEntity.getRoutineEntity().getRoutineId())
                                                .routineName(resultEntity.getRoutineEntity().getRoutineName())
                                                .memberId(resultEntity.getMemberEntity().getMemberId())
                                                .memberName(resultEntity.getMemberEntity().getMemberName())
                                                .doneAt(resultEntity.getDoneAt())
                                                .createdAt(resultEntity.getCreatedAt())
                                                .build())
                                .toList();

                return ResponseEntity.ok().body(listOfResultResponseDto);
        }

        // 기록 삭제
        @Transactional
        public ResponseEntity<Object> deleteResult(Long resultId) {
                if (!resultRepository.existsById(resultId)) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();

                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                } else {
                        resultRepository.deleteById(resultId);

                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(true)
                                        .message("요청을 성공적으로 처리했습니다.")
                                        .build();

                        return ResponseEntity.ok().body(requestSuccessDto);
                }
        }

        // 오늘 날짜의 기록 조회
        @Transactional
        public ResponseEntity<Object> getTodayResults() {
                List<ResultResponseDto> listOfTodayResults = resultRepository.findAllByCurrentDate().stream()
                                .map(resultEntity -> ResultResponseDto.builder()
                                                .resultId(resultEntity.getResultId())
                                                .routineId(resultEntity.getRoutineEntity().getRoutineId())
                                                .routineName(resultEntity.getRoutineEntity().getRoutineName())
                                                .memberId(resultEntity.getMemberEntity().getMemberId())
                                                .memberName(resultEntity.getMemberEntity().getMemberName())
                                                .createdAt(resultEntity.getCreatedAt())
                                                .doneAt(resultEntity.getDoneAt())
                                                .build())
                                .toList();

                return ResponseEntity.ok().body(listOfTodayResults);
        }

        // 특정 사용자의 오늘 날짜의 기록 조회
        @Transactional
        public ResponseEntity<Object> getTodayRoutines(String memberName) {

                MemberEntity memberEntity = memberRepository.findByMemberName(memberName).orElseGet(null);

                if (memberEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                List<TodayRoutinesDto> todayRoutinesDto = new ArrayList<>();
                List<TodayRoutinesDto> incompleteRoutines = new ArrayList<>();
                List<TodayRoutinesDto> completeRoutines = new ArrayList<>();

                // 특정 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
                incompleteRoutines = resultRepository.getActivatedAndIncompleteRoutines(memberEntity);

                // 특정 사용자의 루틴 중 활성화되고 인증된 루틴 조회
                completeRoutines = resultRepository.getActivatedAndCompleteRoutines(memberEntity);

                todayRoutinesDto.addAll(incompleteRoutines);
                todayRoutinesDto.addAll(completeRoutines);

                return ResponseEntity.ok().body(todayRoutinesDto);
        }

        // 모든 사용자의 오늘 날짜의 루틴 완료 여부 조회
        @Transactional
        public ResponseEntity<Object> getAllTodayRoutines() {
                List<TodayRoutinesDto> todayRoutinesDto = new ArrayList<>();
                List<TodayRoutinesDto> incompleteRoutines = new ArrayList<>();
                List<TodayRoutinesDto> completeRoutines = new ArrayList<>();

                // 모든 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
                incompleteRoutines = resultRepository.getAllActivatedAndIncompleteRoutines();

                // 모든 사용자의 루틴 중 활성화되고 인증된 루틴 조회
                completeRoutines = resultRepository.getAllActivatedAndCompleteRoutines();

                todayRoutinesDto.addAll(incompleteRoutines);
                todayRoutinesDto.addAll(completeRoutines);

                return ResponseEntity.ok().body(todayRoutinesDto);
        }

        // 요일별 달성률
        @Transactional
        public ResponseEntity<Object> getDayOfWeekAchievement(String memberName) {

                MemberEntity memberEntity = memberRepository.findByMemberName(memberName).orElseGet(null);

                if (memberEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                List<DayOfWeekAchievementDto> listOfDayOfWeekAchievementDto = new ArrayList<>();

                DayOfWeek[] dayOfWeek = DayOfWeek.values();

                for (int i = 0; i < dayOfWeek.length; i++) {
                        DayOfWeekAchievementDto dayOfWeekAchievementDto = DayOfWeekAchievementDto.builder()
                                        .dayOfWeek(dayOfWeek[i])
                                        .achievement(resultRepository.getDayOfWeekAchievement(memberEntity, i))
                                        .build();

                        listOfDayOfWeekAchievementDto.add(dayOfWeekAchievementDto);
                }

                return ResponseEntity.ok().body(listOfDayOfWeekAchievementDto);
        }

        // 루틴별 달성률
        @Transactional
        public ResponseEntity<Object> getRoutineAchievement(String memberName) {

                MemberEntity memberEntity = memberRepository.findByMemberName(memberName).orElseGet(null);

                if (memberEntity == null) {
                        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                        .success(false)
                                        .message("해당하는 리소스가 없습니다.")
                                        .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
                }

                List<RoutineAchievementDto> routineAchievementDto = resultRepository
                                .getRoutineAchievement(memberEntity);

                return ResponseEntity.ok().body(routineAchievementDto);
        }

        // // 인증 사진 다운
        // @Transactional
        // public List<ResultResponseDto> getProofFiles() {
        // List<ResultResponseDto> resultEntity =
        // resultRepository.findAll().stream().map(ResultResponseDto::new).toList();
        // String filePath =
        // }
}
