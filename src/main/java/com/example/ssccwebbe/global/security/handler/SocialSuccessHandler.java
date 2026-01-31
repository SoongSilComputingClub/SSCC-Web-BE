package com.example.ssccwebbe.global.security.handler;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.ssccwebbe.global.security.UserRoleType;
import com.example.ssccwebbe.global.security.jwt.service.JwtService;
import com.example.ssccwebbe.global.security.jwt.util.JwtUtil;

@Component
@Qualifier("SocialSuccessHandler")
public class SocialSuccessHandler implements AuthenticationSuccessHandler {

    private final Map<UserRoleType, JwtService> jwtServiceMap;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${frontend.cookie.secure}")
    private boolean cookieSecure;

    public SocialSuccessHandler(@Qualifier("preJwtService") JwtService preJwtService) {
        // JWT Service 매핑 (Strategy 패턴)
        this.jwtServiceMap =
                Map.of(
                        UserRoleType.PREUSER, preJwtService
                        // UserRoleType.USER, userJwtService  // 나중에 추가
                        // UserRoleType.ADMIN, adminJwtService  // 나중에 추가
                        );
    }

    // 소셜 로그인 성공시 동작 메서드
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        // username, role
        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // UserRoleType 변환 및 JwtService 선택 (Strategy 패턴)
        UserRoleType roleType = parseRoleType(role);
        JwtService jwtService =
                jwtServiceMap.getOrDefault(
                        roleType, jwtServiceMap.get(UserRoleType.PREUSER)); // 기본값: PREUSER

        // JWT(Refresh) 발급 => 소셜 로그인의 경우 브라우저 리다이렉트 방식으로 토큰 발급이 쿠키 방식으로만 가능
        String refreshToken = JwtUtil.createJwt(username, "ROLE_" + role, false);

        // 발급한 Refresh DB 테이블 저장 (Refresh whitelist)
        jwtService.addRefresh(username, refreshToken);

        // 응답
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(cookieSecure);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(10); // 10초 (프론트에서 발급 후 바로 헤더 전환 로직 진행 예정)

        response.addCookie(refreshCookie);
        response.sendRedirect(frontendUrl + "/cookie"); // 프론트 주소로 redirect
    }

    /** "ROLE_PREUSER" -> UserRoleType.PREUSER 변환 */
    private UserRoleType parseRoleType(String roleString) {
        String role = roleString.replace("ROLE_", ""); // "ROLE_PREUSER" -> "PREUSER"
        try {
            return UserRoleType.valueOf(role); // "PREUSER" -> UserRoleType.PREUSER
        } catch (IllegalArgumentException e) {
            return UserRoleType.PREUSER; // 기본값
        }
    }
}
