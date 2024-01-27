package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miracle.miraclemorningback.dto.ComplaintDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.ComplaintRequestDto;
import com.miracle.miraclemorningback.dto.ComplaintResponseDto;
import com.miracle.miraclemorningback.entity.ComplaintEntity;
import com.miracle.miraclemorningback.repository.ComplaintRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    // 제보 조회
    @Transactional
    public List<ComplaintResponseDto> getComplaints() {
        return complaintRepository.findAll().stream().map(complaintEntity -> ComplaintResponseDto.builder()
                .complaintId(complaintEntity.getComplaintId())
                .memberName(complaintEntity.getMemberName())
                .content(complaintEntity.getContent())
                .createdAt(complaintEntity.getCreatedAt())
                .build())
                .toList();
    }

    // 오류 제보
    @Transactional
    public ComplaintResponseDto complain(ComplaintRequestDto requestDto) {
        ComplaintEntity complaintEntity = ComplaintEntity.builder()
                .memberName(requestDto.getMemberName())
                .content(requestDto.getContent())
                .build();
        complaintRepository.save(complaintEntity);

        return ComplaintResponseDto.builder()
                .complaintId(complaintEntity.getComplaintId())
                .memberName(complaintEntity.getMemberName())
                .content(complaintEntity.getContent())
                .createdAt(complaintEntity.getCreatedAt())
                .build();
    }

    // 제보 삭제
    @Transactional
    public ComplaintDeleteSuccessResponseDto deleteComplaint(Long complaintId) throws Exception {
        complaintRepository.findById(complaintId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않은 아이디입니다."));

        complaintRepository.deleteById(complaintId);
        return ComplaintDeleteSuccessResponseDto.builder().success(true).build();
    }
}
