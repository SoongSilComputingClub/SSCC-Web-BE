package com.example.ssccwebbe.domain.preuser.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.ssccwebbe.domain.preuser.entity.PreUserEntity;
import com.example.ssccwebbe.domain.preuser.entity.SocialProviderType;
import com.example.ssccwebbe.global.security.UserRoleType;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PreUserRepositoryTest {

    @Autowired private PreUserRepository preUserRepository;

    @BeforeEach
    void setUp() {
        preUserRepository.deleteAll();
    }

    @Test
    @DisplayName("소셜 로그인 사용자를 username과 isSocial로 조회할 수 있다")
    void findByUsernameAndIsSocial_SocialUser_Success() {
        // given
        String username = "socialuser@test.com";
        PreUserEntity socialUser =
                PreUserEntity.builder()
                        .username(username)
                        .isSocial(true)
                        .isLock(false)
                        .isAccepted(false)
                        .socialProviderType(SocialProviderType.GOOGLE)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Social User")
                        .email("socialuser@test.com")
                        .build();
        preUserRepository.save(socialUser);

        // when
        Optional<PreUserEntity> result =
                preUserRepository.findByUsernameAndIsSocial(username, true);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
        assertThat(result.get().getIsSocial()).isTrue();
        assertThat(result.get().getSocialProviderType()).isEqualTo(SocialProviderType.GOOGLE);
    }

    @Test
    @DisplayName("일반 로그인 사용자를 username과 isSocial로 조회할 수 있다")
    void findByUsernameAndIsSocial_RegularUser_Success() {
        // given
        String username = "regularuser@test.com";
        PreUserEntity regularUser =
                PreUserEntity.builder()
                        .username(username)
                        .isSocial(false)
                        .isLock(false)
                        .isAccepted(false)
                        .socialProviderType(null)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Regular User")
                        .email("regularuser@test.com")
                        .build();
        preUserRepository.save(regularUser);

        // when
        Optional<PreUserEntity> result =
                preUserRepository.findByUsernameAndIsSocial(username, false);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
        assertThat(result.get().getIsSocial()).isFalse();
        assertThat(result.get().getSocialProviderType()).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 username으로 조회하면 빈 Optional을 반환한다")
    void findByUsernameAndIsSocial_NotExistingUsername_ReturnsEmpty() {
        // given
        String nonExistingUsername = "nonexisting@test.com";

        // when
        Optional<PreUserEntity> result =
                preUserRepository.findByUsernameAndIsSocial(nonExistingUsername, true);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("여러 사용자 중에서 username과 isSocial 조건에 맞는 사용자만 조회된다")
    void findByUsernameAndIsSocial_MultipleUsers_ReturnsMatchingUser() {
        // given
        PreUserEntity socialUser1 =
                PreUserEntity.builder()
                        .username("social1@test.com")
                        .isSocial(true)
                        .isLock(false)
                        .isAccepted(false)
                        .socialProviderType(SocialProviderType.GOOGLE)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Social User 1")
                        .email("social1@test.com")
                        .build();

        PreUserEntity regularUser1 =
                PreUserEntity.builder()
                        .username("regular1@test.com")
                        .isSocial(false)
                        .isLock(false)
                        .isAccepted(false)
                        .socialProviderType(null)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Regular User 1")
                        .email("regular1@test.com")
                        .build();

        PreUserEntity regularUser2 =
                PreUserEntity.builder()
                        .username("regular2@test.com")
                        .isSocial(false)
                        .isLock(false)
                        .isAccepted(false)
                        .socialProviderType(null)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Regular User 2")
                        .email("regular2@test.com")
                        .build();

        preUserRepository.save(socialUser1);
        preUserRepository.save(regularUser1);
        preUserRepository.save(regularUser2);

        // when
        Optional<PreUserEntity> result =
                preUserRepository.findByUsernameAndIsSocial("regular1@test.com", false);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("regular1@test.com");
        assertThat(result.get().getIsSocial()).isFalse();
        assertThat(result.get().getNickname()).isEqualTo("Regular User 1");
    }

    @Test
    @DisplayName("username이 일치하지만 isSocial이 다르면 빈 Optional을 반환한다")
    void findByUsernameAndIsSocial_WrongSocialFlag_ReturnsEmpty() {
        // given
        String username = "user@test.com";
        PreUserEntity socialUser =
                PreUserEntity.builder()
                        .username(username)
                        .isSocial(true)
                        .isLock(false)
                        .isAccepted(false)
                        .socialProviderType(SocialProviderType.GOOGLE)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("User")
                        .email("user@test.com")
                        .build();
        preUserRepository.save(socialUser);

        // when - isSocial이 false로 조회
        Optional<PreUserEntity> result =
                preUserRepository.findByUsernameAndIsSocial(username, false);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("계정이 잠긴 사용자를 username과 isLock으로 조회할 수 있다")
    void findByUsernameAndIsLock_LockedUser_Success() {
        // given
        String username = "lockeduser@test.com";
        PreUserEntity lockedUser =
                PreUserEntity.builder()
                        .username(username)
                        .isSocial(false)
                        .isLock(true)
                        .isAccepted(false)
                        .socialProviderType(null)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Locked User")
                        .email("lockeduser@test.com")
                        .build();
        preUserRepository.save(lockedUser);

        // when
        Optional<PreUserEntity> result = preUserRepository.findByUsernameAndIsLock(username, true);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
        assertThat(result.get().getIsLock()).isTrue();
    }

    @Test
    @DisplayName("계정이 정상인 사용자를 username과 isLock으로 조회할 수 있다")
    void findByUsernameAndIsLock_UnlockedUser_Success() {
        // given
        String username = "normaluser@test.com";
        PreUserEntity normalUser =
                PreUserEntity.builder()
                        .username(username)
                        .isSocial(false)
                        .isLock(false)
                        .isAccepted(false)
                        .socialProviderType(null)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Normal User")
                        .email("normaluser@test.com")
                        .build();
        preUserRepository.save(normalUser);

        // when
        Optional<PreUserEntity> result = preUserRepository.findByUsernameAndIsLock(username, false);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
        assertThat(result.get().getIsLock()).isFalse();
    }

    @Test
    @DisplayName("findByUsernameAndIsLock - 존재하지 않는 username으로 조회하면 빈 Optional을 반환한다")
    void findByUsernameAndIsLock_NotExistingUsername_ReturnsEmpty() {
        // given
        String nonExistingUsername = "notfound@test.com";

        // when
        Optional<PreUserEntity> result =
                preUserRepository.findByUsernameAndIsLock(nonExistingUsername, false);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("여러 사용자 중에서 username과 isLock 조건에 맞는 사용자만 조회된다")
    void findByUsernameAndIsLock_MultipleUsers_ReturnsMatchingUser() {
        // given
        PreUserEntity lockedUser =
                PreUserEntity.builder()
                        .username("locked1@test.com")
                        .isSocial(false)
                        .isLock(true)
                        .isAccepted(false)
                        .socialProviderType(null)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Locked User 1")
                        .email("locked1@test.com")
                        .build();

        PreUserEntity normalUser1 =
                PreUserEntity.builder()
                        .username("normal1@test.com")
                        .isSocial(false)
                        .isLock(false)
                        .isAccepted(false)
                        .socialProviderType(null)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Normal User 1")
                        .email("normal1@test.com")
                        .build();

        PreUserEntity normalUser2 =
                PreUserEntity.builder()
                        .username("normal2@test.com")
                        .isSocial(false)
                        .isLock(false)
                        .isAccepted(false)
                        .socialProviderType(null)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Normal User 2")
                        .email("normal2@test.com")
                        .build();

        preUserRepository.save(lockedUser);
        preUserRepository.save(normalUser1);
        preUserRepository.save(normalUser2);

        // when
        Optional<PreUserEntity> result =
                preUserRepository.findByUsernameAndIsLock("normal1@test.com", false);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("normal1@test.com");
        assertThat(result.get().getIsLock()).isFalse();
        assertThat(result.get().getNickname()).isEqualTo("Normal User 1");
    }

    @Test
    @DisplayName("username이 일치하지만 isLock이 다르면 빈 Optional을 반환한다")
    void findByUsernameAndIsLock_WrongLockFlag_ReturnsEmpty() {
        // given
        String username = "testuser@test.com";
        PreUserEntity lockedUser =
                PreUserEntity.builder()
                        .username(username)
                        .isSocial(false)
                        .isLock(true)
                        .isAccepted(false)
                        .socialProviderType(null)
                        .roleType(UserRoleType.PREUSER)
                        .nickname("Test User")
                        .email("testuser@test.com")
                        .build();
        preUserRepository.save(lockedUser);

        // when - isLock이 false로 조회
        Optional<PreUserEntity> result = preUserRepository.findByUsernameAndIsLock(username, false);

        // then
        assertThat(result).isEmpty();
    }
}
