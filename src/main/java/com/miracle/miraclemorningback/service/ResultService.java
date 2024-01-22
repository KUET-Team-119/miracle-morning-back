package com.miracle.miraclemorningback.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.miracle.miraclemorningback.dto.ResultDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.ResultRequestDto;
import com.miracle.miraclemorningback.dto.ResultResponseDto;
import com.miracle.miraclemorningback.entity.ResultEntity;
import com.miracle.miraclemorningback.repository.ResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultService {

        @Autowired
        private ResultRepository resultRepository;

        // 인증 사진 저장 경로
        private final String DIR_PATH = "/home/chori/workspace/project/miracle-morning/miracle-morning-back/src/main/resources/proofImage/";

        // 전체 기록 조회
        @Transactional(readOnly = true)
        public List<ResultResponseDto> getResults() {
                return resultRepository.findAll().stream()
                                .map(resultEntity -> ResultResponseDto.builder()
                                                .resultId(resultEntity.getResultId())
                                                .routineName(resultEntity.getRoutineName())
                                                .memberName(resultEntity.getMemberName())
                                                .createdAt(resultEntity.getCreatedAt())
                                                .build())
                                .toList();
        }

        // 기록 추가
        @Transactional
        public ResultResponseDto addResult(ResultRequestDto requestDto, MultipartFile file) throws IOException {

                // 파일 업로드 시간
                LocalDateTime dateTime = LocalDateTime.now();
                String timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateTime);

                // 파일명: 닉네임 + 루틴명 + 업로드 시간 + 원본 파일명
                String fileName = requestDto.getMemberName() + "_" + requestDto.getRoutineName() + "_" + timeStamp
                                + "_" + file.getOriginalFilename();

                // 파일 경로
                String filePath = DIR_PATH + fileName;
                file.transferTo(new File(DIR_PATH, fileName));

                ResultEntity resultEntity = ResultEntity.builder()
                                .memberName(requestDto.getMemberName())
                                .routineName(requestDto.getRoutineName())
                                .filePath(filePath)
                                .build();
                resultRepository.save(resultEntity);

                return ResultResponseDto.builder()
                                .resultId(resultEntity.getResultId())
                                .routineName(resultEntity.getRoutineName())
                                .memberName(resultEntity.getMemberName())
                                .createdAt(resultEntity.getCreatedAt())
                                .build();
        }

        // 특정 기록 검색
        // TODO resultId에서 날짜 데이터로 변경 / 쿼리 파라미터 형식으로 바꾸는 방향도 고려
        @Transactional
        public ResultResponseDto getResult(Long resultId) {
                return resultRepository.findById(resultId)
                                .map(resultEntity -> ResultResponseDto.builder()
                                                .resultId(resultEntity.getResultId())
                                                .routineName(resultEntity.getRoutineName())
                                                .memberName(resultEntity.getMemberName())
                                                .createdAt(resultEntity.getCreatedAt())
                                                .build())
                                .orElseThrow(
                                                // 아이디가 존재하지 않으면 예외 처리
                                                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));
        }

        // 기록 삭제
        @Transactional
        public ResultDeleteSuccessResponseDto deleteResult(Long resultId) throws Exception {
                resultRepository.findById(resultId).orElseThrow(
                                // 아이디가 존재하지 않으면 예외 처리
                                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));

                resultRepository.deleteById(resultId);
                return ResultDeleteSuccessResponseDto.builder().success(true).build();
        }

        // 오늘 날짜의 기록만 조회
        @Transactional
        public List<ResultResponseDto> getTodayResult() {
                return resultRepository.findAllByCurrentDate().stream()
                                .map(resultEntity -> ResultResponseDto.builder()
                                                .resultId(resultEntity.getResultId())
                                                .routineName(resultEntity.getRoutineName())
                                                .memberName(resultEntity.getMemberName())
                                                .createdAt(resultEntity.getCreatedAt())
                                                .build())
                                .toList();
        }

        // // 인증 사진 다운
        // @Transactional
        // public List<ResultResponseDto> getProofFiles() {
        // List<ResultResponseDto> resultEntity =
        // resultRepository.findAll().stream().map(ResultResponseDto::new).toList();
        // String filePath =
        // }
}
