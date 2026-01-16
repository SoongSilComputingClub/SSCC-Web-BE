package com.example.SSCC_Web_BE.global.apiPayload.code.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	HttpStatus getHttpStatus();
	String getCode();
	String getMessage();
}