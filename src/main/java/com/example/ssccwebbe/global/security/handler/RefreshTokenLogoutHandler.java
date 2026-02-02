package com.example.ssccwebbe.global.security.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.StringUtils;

import com.example.ssccwebbe.global.security.jwt.service.JwtService;
import com.example.ssccwebbe.global.security.jwt.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefreshTokenLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;

    public RefreshTokenLogoutHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        try {
            String body =
                    new BufferedReader(new InputStreamReader(request.getInputStream()))
                            .lines()
                            .reduce("", String::concat);

            if (!StringUtils.hasText(body)) {
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);
            String refreshToken =
                    jsonNode.has("refreshToken") ? jsonNode.get("refreshToken").asText() : null;

            // 유효성 검증
            if (refreshToken == null) {
                return;
            }
            Boolean isValid = JwtUtil.isValid(refreshToken, false);
            if (!isValid) {
                return;
            }

            // Refresh 토큰 삭제
            jwtService.removeRefresh(refreshToken);

        } catch (IOException e) {
            // 조용히 실패 - logout 자체는 계속 진행
            log.warn("Failed to read refresh token during logout", e);
        }
    }
}
