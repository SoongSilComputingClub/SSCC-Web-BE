package com.example.SSCC_Web_BE.global.apiPayload.exception;

import com.example.SSCC_Web_BE.global.apiPayload.code.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SsccException extends RuntimeException {
    private final ErrorCode errorCode;
}
