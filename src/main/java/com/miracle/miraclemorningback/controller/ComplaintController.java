package com.miracle.miraclemorningback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.miraclemorningback.dto.ComplaintRequestDto;
import com.miracle.miraclemorningback.dto.ComplaintResponseDto;
import com.miracle.miraclemorningback.entity.UserDetailsImpl;
import com.miracle.miraclemorningback.service.ComplaintService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    // 제보 조회
    @GetMapping("/api/complaints")
    public List<ComplaintResponseDto> getComplaints() {
        return complaintService.getComplaints();
    }

    // 오류 제보
    @PostMapping("/api/complaint")
    public ResponseEntity<Object> complain(Authentication authentication, @RequestBody ComplaintRequestDto requestDto) {
        String memberName = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return complaintService.complain(memberName, requestDto);
    }

    // 제보 삭제
    @DeleteMapping("/api/complaint/{complaintId}")
    public ResponseEntity<Object> deleteComplaint(@PathVariable Long complaintId) {
        return complaintService.deleteComplaint(complaintId);
    }
}
