package com.example.ssccwebbe.domain.preuser.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.ssccwebbe.domain.preuser.dto.PreUserResponseDto;
import com.example.ssccwebbe.domain.preuser.entity.PreUserEntity;
import com.example.ssccwebbe.domain.preuser.entity.SocialProviderType;
import com.example.ssccwebbe.domain.preuser.repository.PreUserRepository;
import com.example.ssccwebbe.global.security.UserRoleType;

@ExtendWith(MockitoExtension.class)
class PreUserServiceImplTest {

    @Mock private PreUserRepository preUserRepository;

    @Mock private SecurityContext securityContext;

    @Mock private Authentication authentication;

    @InjectMocks private PreUserServiceImpl preUserService;

    @Test
    @DisplayName("readPreUser - 일반 사용자 정보를 정상적으로 조회한다")
    void readPreUser_RegularUser_Success() {
        // given
        String username = "testuser@test.com";
        String email = "testuser@test.com";
        String nickname = "Test User";

        PreUserEntity userEntity =
                PreUserEntity.builder()
                        .username(username)
                        .email(email)
                        .nickname(nickname)
                        .isSocial(false)
                        .isLock(false)
                        .roleType(UserRoleType.USER)
                        .build();

        // SecurityContext 모킹
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(preUserRepository.findByUsernameAndIsLock(username, false))
                .thenReturn(Optional.of(userEntity));

        // when
        PreUserResponseDto result = preUserService.readPreUser();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getNickname()).isEqualTo(nickname);
        assertThat(result.getSocial()).isFalse();

        verify(preUserRepository, times(1)).findByUsernameAndIsLock(username, false);
    }

    @Test
    @DisplayName("readPreUser - 소셜 로그인 사용자 정보를 정상적으로 조회한다")
    void readPreUser_SocialUser_Success() {
        // given
        String username = "GOOGLE_123456789";
        String email = "socialuser@gmail.com";
        String nickname = "Social User";

        PreUserEntity userEntity =
                PreUserEntity.builder()
                        .username(username)
                        .email(email)
                        .nickname(nickname)
                        .isSocial(true)
                        .isLock(false)
                        .socialProviderType(SocialProviderType.GOOGLE)
                        .roleType(UserRoleType.USER)
                        .build();

        // SecurityContext 모킹
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(preUserRepository.findByUsernameAndIsLock(username, false))
                .thenReturn(Optional.of(userEntity));

        // when
        PreUserResponseDto result = preUserService.readPreUser();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getNickname()).isEqualTo(nickname);
        assertThat(result.getSocial()).isTrue();

        verify(preUserRepository, times(1)).findByUsernameAndIsLock(username, false);
    }

    @Test
    @DisplayName("readPreUser - 존재하지 않는 사용자 조회 시 UsernameNotFoundException 발생")
    void readPreUser_UserNotFound_ThrowsException() {
        // given
        String username = "nonexistent@test.com";

        // SecurityContext 모킹
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(preUserRepository.findByUsernameAndIsLock(username, false))
                .thenReturn(Optional.empty());

        // when & then
        org.junit.jupiter.api.Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> preUserService.readPreUser(),
                "해당 유저를 찾을 수 없습니다: " + username);

        verify(preUserRepository, times(1)).findByUsernameAndIsLock(username, false);
    }

    @Test
    @DisplayName("readPreUser - 계정이 잠긴 사용자는 조회되지 않는다")
    void readPreUser_LockedUser_NotFound() {
        // given
        String username = "lockeduser@test.com";

        // SecurityContext 모킹
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // isLock=true인 사용자는 조회되지 않음
        when(preUserRepository.findByUsernameAndIsLock(username, false))
                .thenReturn(Optional.empty());

        // when & then
        org.junit.jupiter.api.Assertions.assertThrows(
                UsernameNotFoundException.class, () -> preUserService.readPreUser());

        verify(preUserRepository, times(1)).findByUsernameAndIsLock(username, false);
    }

    @Test
    @DisplayName("readPreUser - PREUSER 권한 사용자도 정상적으로 조회된다")
    void readPreUser_PreUserRole_Success() {
        // given
        String username = "preuser@test.com";
        String email = "preuser@test.com";
        String nickname = "Pre User";

        PreUserEntity userEntity =
                PreUserEntity.builder()
                        .username(username)
                        .email(email)
                        .nickname(nickname)
                        .isSocial(false)
                        .isLock(false)
                        .roleType(UserRoleType.PREUSER)
                        .build();

        // SecurityContext 모킹
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(preUserRepository.findByUsernameAndIsLock(username, false))
                .thenReturn(Optional.of(userEntity));

        // when
        PreUserResponseDto result = preUserService.readPreUser();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getNickname()).isEqualTo(nickname);
        assertThat(result.getSocial()).isFalse();
    }

    @Test
    @DisplayName("readPreUser - ADMIN 권한 사용자도 정상적으로 조회된다")
    void readPreUser_AdminRole_Success() {
        // given
        String username = "admin@test.com";
        String email = "admin@test.com";
        String nickname = "Admin User";

        PreUserEntity userEntity =
                PreUserEntity.builder()
                        .username(username)
                        .email(email)
                        .nickname(nickname)
                        .isSocial(false)
                        .isLock(false)
                        .roleType(UserRoleType.ADMIN)
                        .build();

        // SecurityContext 모킹
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(preUserRepository.findByUsernameAndIsLock(username, false))
                .thenReturn(Optional.of(userEntity));

        // when
        PreUserResponseDto result = preUserService.readPreUser();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getNickname()).isEqualTo(nickname);
        assertThat(result.getSocial()).isFalse();
    }
}
