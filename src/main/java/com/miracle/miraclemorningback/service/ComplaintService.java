package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.dto.ComplaintRequestDto;
import com.miracle.miraclemorningback.dto.ComplaintResponseDto;
import com.miracle.miraclemorningback.dto.RequestSuccessDto;
import com.miracle.miraclemorningback.entity.ComplaintEntity;
import com.miracle.miraclemorningback.repository.ComplaintRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    // 제보 조회
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getComplaints() {
        List<ComplaintResponseDto> listOfComplaints = complaintRepository.findAll().stream()
                .map(complaintEntity -> ComplaintResponseDto.builder()
                        .complaintId(complaintEntity.getComplaintId())
                        .memberName(complaintEntity.getMemberName())
                        .content(complaintEntity.getContent())
                        .createdAt(complaintEntity.getCreatedAt())
                        .build())
                .toList();

        return ResponseEntity.ok().body(listOfComplaints);
    }

    // 오류 제보
    @Transactional
    public ResponseEntity<Object> complain(String memberName, ComplaintRequestDto requestDto) {
        ComplaintEntity complaintEntity = ComplaintEntity.builder()
                .memberName(memberName)
                .content(requestDto.getContent())
                .build();
        complaintRepository.save(complaintEntity);

        RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                .success(true)
                .message("요청이 성공적으로 처리되었습니다.")
                .build();

        return ResponseEntity.ok().body(requestSuccessDto);
    }

    // 제보 삭제
    @Transactional
    public ResponseEntity<Object> deleteComplaint(Long complaintId) {
        if (!complaintRepository.existsById(complaintId)) {
            RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                    .success(false)
                    .message("해당하는 리소스가 없습니다.")
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestSuccessDto);
        } else {
            complaintRepository.deleteById(complaintId);
            RequestSuccessDto requestSuccessDto = RequestSuccessDto.builder()
                    .success(true)
                    .message("요청이 성공적으로 처리되었습니다.")
                    .build();

            return ResponseEntity.ok().body(requestSuccessDto);
        }
    }
}
