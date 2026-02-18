package com.example.ssccwebbe.apitest.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ssccwebbe.apitest.dto.TestTokenResponseDto;
import com.example.ssccwebbe.domain.user.entity.UserEntity;
import com.example.ssccwebbe.domain.user.repository.PreUserRepository;
import com.example.ssccwebbe.global.security.UserRoleType;
import com.example.ssccwebbe.global.security.jwt.service.JwtService;
import com.example.ssccwebbe.global.security.jwt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestService {

    private final PreUserRepository preUserRepository;
    private final JwtService jwtService;

    @Transactional
    public TestTokenResponseDto createOrGetTokens(String username) {

        // 1. 유저 조회 (isSocial=false인 유저)
        Optional<UserEntity> existingUser =
                preUserRepository.findByUsernameAndIsSocial(username, false);

        UserEntity user;

        // 2. 없으면 새로 생성
        if (existingUser.isEmpty()) {
            user =
                    UserEntity.builder()
                            .username(username)
                            .email(username + "@test.com")
                            .nickname(username)
                            .isSocial(false)
                            .isLock(false)
                            .isAccepted(true)
                            .roleType(UserRoleType.PREUSER)
                            .socialProviderType(null)
                            .build();

            preUserRepository.save(user);
        } else {
            user = existingUser.get();
        }

        // 3. 토큰 생성
        String role = "ROLE_" + user.getRoleType().name();
        String accessToken = JwtUtil.createJwt(username, role, true);
        String refreshToken = JwtUtil.createJwt(username, role, false);

        // 4. Refresh 토큰 저장
        jwtService.addRefresh(username, refreshToken);

        // 5. 응답 반환
        return TestTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
