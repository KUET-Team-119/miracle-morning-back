package com.miracle.miraclemorningback.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.miracle.miraclemorningback.dto.RequestSuccessDto;
import com.miracle.miraclemorningback.dto.ResultRequestDto;
import com.miracle.miraclemorningback.dto.ResultResponseDto;
import com.miracle.miraclemorningback.dto.TodayRoutinesDto;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.ResultEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;
import com.miracle.miraclemorningback.repository.ResultRepository;
import com.miracle.miraclemorningback.repository.TodayRoutinesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultService {

        @Autowired
        private MemberRepository memberRepository;

        @Autowired
        private ResultRepository resultRepository;

        @Autowired
        private TodayRoutinesRepository todayRoutinesRepository;

        // 인증 사진 저장 경로
        @Value("${images.path}")
        private String DIR_PATH;

        // 전체 기록 조회
        @Transactional(readOnly = true)
        public List<ResultResponseDto> getResults() {
                return resultRepository.findAll().stream()
                                .map(resultEntity -> ResultResponseDto.builder()
                                                .resultId(resultEntity.getResultId())
                                                .routineName(resultEntity.getRoutineName())
                                                .memberName(resultEntity.getMemberEntity().getMemberName())
                                                .doneAt(resultEntity.getDoneAt())
                                                .createdAt(resultEntity.getCreatedAt())
                                                .build())
                                .toList();
        }

        // 루틴 인증을 위한 기록 추가
        @Transactional
        public ResponseEntity<Object> updateResult(String memberName, ResultRequestDto requestDto, MultipartFile file)
                        throws IOException {

                MemberEntity memberEntity = memberRepository.findByMemberName(memberName).get();

                ResultEntity resultEntity = resultRepository.findByRoutineNameAndMemberEntityAndCurrentDate(
                                requestDto.getRoutineName(), memberEntity).orElse(null);

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
                String fileName = memberName + "_" + requestDto.getRoutineName() + "_" + timeStamp
                                + "_" + file.getOriginalFilename();

                // 파일 경로
                String filePath = DIR_PATH + fileName;
                file.transferTo(new File(DIR_PATH, fileName));

                // 기록 업데이트
                resultRepository.updateResult(requestDto.getRoutineName(),
                                memberEntity,
                                requestDto.getDoneAt(),
                                filePath);

                RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                                .success(true)
                                .message("요청을 성공적으로 처리했습니다.")
                                .build();

                return ResponseEntity.ok().body(requestSuccessDto);
        }

        // 특정 기록 검색
        // TODO resultId에서 날짜 데이터로 변경 / 쿼리 파라미터 형식으로 바꾸는 방향도 고려
        @Transactional
        public ResultResponseDto getResult(Long resultId) {
                return resultRepository.findById(resultId)
                                .map(resultEntity -> ResultResponseDto.builder()
                                                .resultId(resultEntity.getResultId())
                                                .routineName(resultEntity.getRoutineName())
                                                .memberName(resultEntity.getMemberEntity().getMemberName())
                                                .createdAt(resultEntity.getCreatedAt())
                                                .doneAt(resultEntity.getDoneAt())
                                                .build())
                                .orElseThrow(
                                                // 아이디가 존재하지 않으면 예외 처리
                                                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));
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

        // 오늘 날짜의 기록만 조회
        @Transactional
        public List<ResultResponseDto> getTodayResult() {
                return resultRepository.findAllByCurrentDate().stream()
                                .map(resultEntity -> ResultResponseDto.builder()
                                                .resultId(resultEntity.getResultId())
                                                .routineName(resultEntity.getRoutineName())
                                                .memberName(resultEntity.getMemberEntity().getMemberName())
                                                .createdAt(resultEntity.getCreatedAt())
                                                .doneAt(resultEntity.getDoneAt())
                                                .build())
                                .toList();
        }

        // 특정 사용자의 오늘 날짜의 기록만 조회
        @Transactional
        public List<TodayRoutinesDto> getTodayRoutines(String memberName) {

                MemberEntity memberEntity = memberRepository.findByMemberName(memberName).get();

                List<TodayRoutinesDto> todayRoutinesDto = new ArrayList<>();
                List<TodayRoutinesDto> incompleteRoutines = new ArrayList<>();
                List<TodayRoutinesDto> completeRoutines = new ArrayList<>();

                // 특정 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
                incompleteRoutines = todayRoutinesRepository
                                .getActivatedAndIncompleteRoutines(memberEntity.getMemberId()).stream()
                                .map(todayRoutinesEntity -> TodayRoutinesDto.builder()
                                                .routineId(todayRoutinesEntity.getRoutine_id())
                                                .routineName(todayRoutinesEntity.getRoutine_name())
                                                .memberName(memberEntity.getMemberName())
                                                .strategy(todayRoutinesEntity.getStrategy())
                                                .certification(todayRoutinesEntity.getCertification())
                                                .startTime(todayRoutinesEntity.getStart_time())
                                                .endTime(todayRoutinesEntity.getEnd_time())
                                                .createdAt(todayRoutinesEntity.getCreated_at())
                                                .doneAt(todayRoutinesEntity.getDone_at())
                                                .complete(false)
                                                .build())
                                .toList();

                // 특정 사용자의 루틴 중 활성화되고 인증된 루틴 조회
                completeRoutines = todayRoutinesRepository.getActivatedAndCompleteRoutines(memberEntity.getMemberId())
                                .stream()
                                .map(todayRoutinesEntity -> TodayRoutinesDto.builder()
                                                .routineId(todayRoutinesEntity.getRoutine_id())
                                                .routineName(todayRoutinesEntity.getRoutine_name())
                                                .memberName(memberEntity.getMemberName())
                                                .strategy(todayRoutinesEntity.getStrategy())
                                                .certification(todayRoutinesEntity.getCertification())
                                                .startTime(todayRoutinesEntity.getStart_time())
                                                .endTime(todayRoutinesEntity.getEnd_time())
                                                .createdAt(todayRoutinesEntity.getCreated_at())
                                                .doneAt(todayRoutinesEntity.getDone_at())
                                                .complete(true)
                                                .build())
                                .toList();

                todayRoutinesDto.addAll(incompleteRoutines);
                todayRoutinesDto.addAll(completeRoutines);

                return todayRoutinesDto;
        }

        // 모든 사용자의 오늘 날짜의 루틴 완료 여부 조회
        @Transactional
        public List<TodayRoutinesDto> getAllTodayRoutines() {
                List<TodayRoutinesDto> todayRoutinesDto = new ArrayList<>();
                List<TodayRoutinesDto> incompleteRoutines = new ArrayList<>();
                List<TodayRoutinesDto> completeRoutines = new ArrayList<>();

                // 모든 사용자의 루틴 중 활성화되고 인증되지 않은 루틴 조회
                incompleteRoutines = todayRoutinesRepository.getAllActivatedAndIncompleteRoutines().stream()
                                .map(todayRoutinesEntity -> TodayRoutinesDto.builder()
                                                .routineId(todayRoutinesEntity.getRoutine_id())
                                                .routineName(todayRoutinesEntity.getRoutine_name())
                                                .memberName(memberRepository
                                                                .findById(todayRoutinesEntity.getMember_id()).get()
                                                                .getMemberName())
                                                .strategy(todayRoutinesEntity.getStrategy())
                                                .certification(todayRoutinesEntity.getCertification())
                                                .startTime(todayRoutinesEntity.getStart_time())
                                                .endTime(todayRoutinesEntity.getEnd_time())
                                                .createdAt(todayRoutinesEntity.getCreated_at())
                                                .doneAt(todayRoutinesEntity.getDone_at())
                                                .complete(false)
                                                .build())
                                .toList();

                // 모든 사용자의 루틴 중 활성화되고 인증된 루틴 조회
                completeRoutines = todayRoutinesRepository.getAllActivatedAndCompleteRoutines().stream()
                                .map(todayRoutinesEntity -> TodayRoutinesDto.builder()
                                                .routineId(todayRoutinesEntity.getRoutine_id())
                                                .routineName(todayRoutinesEntity.getRoutine_name())
                                                .memberName(memberRepository
                                                                .findById(todayRoutinesEntity.getMember_id()).get()
                                                                .getMemberName())
                                                .strategy(todayRoutinesEntity.getStrategy())
                                                .certification(todayRoutinesEntity.getCertification())
                                                .startTime(todayRoutinesEntity.getStart_time())
                                                .endTime(todayRoutinesEntity.getEnd_time())
                                                .createdAt(todayRoutinesEntity.getCreated_at())
                                                .doneAt(todayRoutinesEntity.getDone_at())
                                                .complete(true)
                                                .build())
                                .toList();

                todayRoutinesDto.addAll(incompleteRoutines);
                todayRoutinesDto.addAll(completeRoutines);

                return todayRoutinesDto;
        }

        // // 인증 사진 다운
        // @Transactional
        // public List<ResultResponseDto> getProofFiles() {
        // List<ResultResponseDto> resultEntity =
        // resultRepository.findAll().stream().map(ResultResponseDto::new).toList();
        // String filePath =
        // }
}
