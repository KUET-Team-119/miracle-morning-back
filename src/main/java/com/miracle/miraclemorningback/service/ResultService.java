package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 전체 기록 조회
    @Transactional(readOnly = true)
    public List<ResultResponseDto> getResults() {
        return resultRepository.findAll().stream().map(ResultResponseDto::new).toList();
    }

    // 기록 추가
    @Transactional
    public ResultResponseDto addResult(ResultRequestDto requestDto) {
        ResultEntity resultEntity = new ResultEntity(requestDto);
        resultRepository.save(resultEntity);
        return new ResultResponseDto(resultEntity);
    }

    // 특정 기록 검색
    @Transactional
    public ResultResponseDto getResult(Long result_id) {
        return resultRepository.findById(result_id).map(ResultResponseDto::new).orElseThrow(
                // 아이디가 존재하지 않으면 예외 처리
                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));
    }

    // 기록 삭제
    @Transactional
    public ResultDeleteSuccessResponseDto deleteResult(Long result_id) throws Exception {
        resultRepository.findById(result_id).orElseThrow(
                // 아이디가 존재하지 않으면 예외 처리
                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));

        resultRepository.deleteById(result_id);
        return new ResultDeleteSuccessResponseDto(true);
    }
}
