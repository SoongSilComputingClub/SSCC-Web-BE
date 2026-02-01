package com.example.ssccwebbe.domain.preuser.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.ssccwebbe.domain.preuser.entity.PreUserRefreshEntity;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PreUserRefreshRepositoryTest {

    @Autowired private PreUserRefreshRepository preUserRefreshRepository;

    @BeforeEach
    void setUp() {
        preUserRefreshRepository.deleteAll();
    }

    @Test
    @DisplayName("refresh token이 존재할 때 true를 반환한다")
    void existsByRefresh_ExistingToken_ReturnsTrue() {
        // given
        String refreshToken = "test-refresh-token-12345";
        PreUserRefreshEntity refreshEntity =
                PreUserRefreshEntity.builder()
                        .username("user@test.com")
                        .refresh(refreshToken)
                        .build();
        preUserRefreshRepository.save(refreshEntity);

        // when
        Boolean exists = preUserRefreshRepository.existsByRefresh(refreshToken);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("refresh token이 존재하지 않을 때 false를 반환한다")
    void existsByRefresh_NonExistingToken_ReturnsFalse() {
        // given
        String nonExistingToken = "non-existing-token";

        // when
        Boolean exists = preUserRefreshRepository.existsByRefresh(nonExistingToken);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("여러 refresh token 중에서 특정 token의 존재 여부를 정확히 확인한다")
    void existsByRefresh_MultipleTokens_ReturnsCorrectResult() {
        // given
        String token1 = "refresh-token-1";
        String token2 = "refresh-token-2";
        String token3 = "refresh-token-3";

        PreUserRefreshEntity refresh1 =
                PreUserRefreshEntity.builder().username("user1@test.com").refresh(token1).build();

        PreUserRefreshEntity refresh2 =
                PreUserRefreshEntity.builder().username("user2@test.com").refresh(token2).build();

        preUserRefreshRepository.save(refresh1);
        preUserRefreshRepository.save(refresh2);

        // when
        Boolean exists1 = preUserRefreshRepository.existsByRefresh(token1);
        Boolean exists2 = preUserRefreshRepository.existsByRefresh(token2);
        Boolean exists3 = preUserRefreshRepository.existsByRefresh(token3);

        // then
        assertThat(exists1).isTrue();
        assertThat(exists2).isTrue();
        assertThat(exists3).isFalse();
    }

    @Test
    @DisplayName("같은 username에 대해 여러 refresh token이 있을 때 각각 구분하여 확인한다")
    void existsByRefresh_SameUserMultipleTokens_ReturnsCorrectResult() {
        // given
        String username = "user@test.com";
        String oldToken = "old-refresh-token";
        String newToken = "new-refresh-token";

        PreUserRefreshEntity oldRefresh =
                PreUserRefreshEntity.builder().username(username).refresh(oldToken).build();

        PreUserRefreshEntity newRefresh =
                PreUserRefreshEntity.builder().username(username).refresh(newToken).build();

        preUserRefreshRepository.save(oldRefresh);
        preUserRefreshRepository.save(newRefresh);

        // when
        Boolean oldExists = preUserRefreshRepository.existsByRefresh(oldToken);
        Boolean newExists = preUserRefreshRepository.existsByRefresh(newToken);

        // then
        assertThat(oldExists).isTrue();
        assertThat(newExists).isTrue();
    }

    @Test
    @DisplayName("빈 데이터베이스에서 조회하면 false를 반환한다")
    void existsByRefresh_EmptyDatabase_ReturnsFalse() {
        // given - setUp()에서 이미 deleteAll() 호출됨

        // when
        Boolean exists = preUserRefreshRepository.existsByRefresh("any-token");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("긴 refresh token도 정상적으로 조회할 수 있다")
    void existsByRefresh_LongToken_Success() {
        // given
        String longToken = "a".repeat(500); // 512자 제한 내에서
        PreUserRefreshEntity refreshEntity =
                PreUserRefreshEntity.builder().username("user@test.com").refresh(longToken).build();
        preUserRefreshRepository.save(refreshEntity);

        // when
        Boolean exists = preUserRefreshRepository.existsByRefresh(longToken);

        // then
        assertThat(exists).isTrue();
    }
}
