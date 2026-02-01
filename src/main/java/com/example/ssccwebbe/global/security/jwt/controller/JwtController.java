package com.example.ssccwebbe.global.security.jwt.controller;

import com.example.ssccwebbe.global.security.jwt.dto.JwtResponseDto;
import com.example.ssccwebbe.global.security.jwt.dto.RefreshRequestDto;
import com.example.ssccwebbe.global.security.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // 소셜 로그인의 결과로 쿠키로 받은 Refresh 토큰을 가져오는 경우, Access 토큰과 Refresh 토큰을 헤더에 담아 응답으로 제공함
    @PostMapping(value = "/jwt/exchange", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JwtResponseDto jwtExchangeApi(HttpServletRequest request, HttpServletResponse response) {
        return jwtService.cookie2Header(request, response);
    }

    // Refresh 토큰으로 Access 토큰과 Refresh 토큰을 재발급 해줌 (Rotate 포함)
    @PostMapping(value = "/jwt/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JwtResponseDto jwtRefreshApi(@Validated @RequestBody RefreshRequestDto dto) {
        return jwtService.refreshRotate(dto);
    }

}