package com.example.ssccwebbe.domain.applyform.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.ssccwebbe.domain.applyform.entity.ApplyFormStatus;

// 지원서 조회 응답을 위한 DTO
public record ApplyFormReadResponse(
        Long applyFormId,
        String username,
        String applicantName,
        String department,
        String studentNo,
        Integer grade,
        String phone,
        String gender,
        String introduce,
        String codingLevel,
        String techStackText,
        ApplyFormStatus status,
        List<InterviewTime> interviewTimes) {
    public record InterviewTime(LocalDate date, LocalTime startTime, LocalTime endTime) {}
}
