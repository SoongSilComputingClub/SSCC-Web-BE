package com.example.ssccwebbe.global.security.jwt.code;

import org.springframework.http.HttpStatus;

import com.example.ssccwebbe.global.apipayload.code.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorCode implements ErrorCode {

    // JWT 관련 401 UNAUTHORIZED 에러
    COOKIE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT4001", "쿠키가 존재하지 않습니다."),
    REFRESH_TOKEN_COOKIE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT4002", "refreshToken 쿠키가 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "JWT4003", "유효하지 않은 refreshToken입니다."),
    REFRESH_TOKEN_NOT_IN_WHITELIST(
            HttpStatus.UNAUTHORIZED, "JWT4004", "화이트리스트에 없는 refreshToken입니다."),

    // JWT 관련 403 FORBIDDEN 에러
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "JWT4031", "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "JWT4032", "유효하지 않은 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
