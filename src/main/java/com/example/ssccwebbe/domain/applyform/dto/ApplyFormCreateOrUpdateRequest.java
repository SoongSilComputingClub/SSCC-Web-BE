package com.example.ssccwebbe.domain.applyform.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApplyFormCreateOrUpdateRequest(
	@NotBlank String applicantName,
	@NotBlank String department,
	@NotBlank String studentNo,
	@NotNull Integer grade,
	@NotBlank String phone,
	@NotBlank String gender,
	@NotBlank String introduce,
	@NotBlank String codingLevel,
	@NotBlank String techStackText,
	@NotNull List<InterviewTime> interviewTimes
) {
	public record InterviewTime(
		@NotNull LocalDate date,
		@NotNull LocalTime startTime,
		@NotNull LocalTime endTime
	) {}
}