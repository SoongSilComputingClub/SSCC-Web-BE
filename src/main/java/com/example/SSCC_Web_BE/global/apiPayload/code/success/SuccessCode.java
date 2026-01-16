package com.example.SSCC_Web_BE.global.apiPayload.code.success;


import org.springframework.http.HttpStatus;

public interface SuccessCode {
	HttpStatus getHttpStatus();

	String getCode();

	String getMessage();
}
