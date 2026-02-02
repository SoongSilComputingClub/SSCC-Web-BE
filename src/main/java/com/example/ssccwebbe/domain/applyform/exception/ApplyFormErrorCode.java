package com.example.ssccwebbe.domain.applyform.exception;

import org.springframework.http.HttpStatus;

import com.example.ssccwebbe.global.apipayload.code.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyFormErrorCode implements ErrorCode {

	APPLY_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLYFORM404", "작성된 지원서가 없습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "APPLYFORM401", "인증 정보가 없습니다."),
	INVALID_INTERVIEW_TIMES(HttpStatus.BAD_REQUEST, "APPLYFORM400", "면접 희망 시간이 올바르지 않습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}