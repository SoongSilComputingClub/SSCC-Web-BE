package com.example.ssccwebbe.global.security.jwt.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.ssccwebbe.global.security.jwt.util.JwtUtil;

// JWT 검증 필터
public class JwtFilter extends OncePerRequestFilter {

    // Request 헤더에서 accessToken 을 추출하여 인증, 인가를 처리함
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new ServletException("Invalid JWT token");
        }

        // 토큰 파싱
        String accessToken = authorization.split(" ")[1];

        // 토큰 검증
        if (JwtUtil.isValid(accessToken, true)) {

            String username = JwtUtil.getUsername(accessToken);
            String role = JwtUtil.getRole(accessToken);

            List<GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority(role));

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"토큰 만료 또는 유효하지 않은 토큰\"}");
            return;
        }
    }
}
