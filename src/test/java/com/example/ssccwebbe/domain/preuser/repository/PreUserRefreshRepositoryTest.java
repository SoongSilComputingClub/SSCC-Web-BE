package com.example.ssccwebbe.domain.preuser.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

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

    @Test
    @DisplayName("refresh token으로 엔티티를 삭제할 수 있다")
    void deleteByRefresh_ExistingToken_Success() {
        // given
        String refreshToken = "delete-test-token";
        PreUserRefreshEntity refreshEntity =
                PreUserRefreshEntity.builder()
                        .username("user@test.com")
                        .refresh(refreshToken)
                        .build();
        preUserRefreshRepository.save(refreshEntity);

        // when
        preUserRefreshRepository.deleteByRefresh(refreshToken);

        // then
        Boolean exists = preUserRefreshRepository.existsByRefresh(refreshToken);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 refresh token을 삭제해도 에러가 발생하지 않는다")
    void deleteByRefresh_NonExistingToken_NoError() {
        // given
        String nonExistingToken = "non-existing-token";

        // when & then - 에러 없이 정상 실행되어야 함
        preUserRefreshRepository.deleteByRefresh(nonExistingToken);
    }

    @Test
    @DisplayName("여러 엔티티 중 특정 refresh token만 삭제하고 다른 엔티티는 유지된다")
    void deleteByRefresh_MultipleTokens_DeletesOnlyTarget() {
        // given
        String token1 = "token-to-delete";
        String token2 = "token-to-keep-1";
        String token3 = "token-to-keep-2";

        PreUserRefreshEntity refresh1 =
                PreUserRefreshEntity.builder().username("user1@test.com").refresh(token1).build();

        PreUserRefreshEntity refresh2 =
                PreUserRefreshEntity.builder().username("user2@test.com").refresh(token2).build();

        PreUserRefreshEntity refresh3 =
                PreUserRefreshEntity.builder().username("user3@test.com").refresh(token3).build();

        preUserRefreshRepository.save(refresh1);
        preUserRefreshRepository.save(refresh2);
        preUserRefreshRepository.save(refresh3);

        // when
        preUserRefreshRepository.deleteByRefresh(token1);

        // then
        assertThat(preUserRefreshRepository.existsByRefresh(token1)).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh(token2)).isTrue();
        assertThat(preUserRefreshRepository.existsByRefresh(token3)).isTrue();
    }

    @Test
    @DisplayName("같은 username의 여러 refresh token 중 하나만 삭제하고 나머지는 유지된다")
    void deleteByRefresh_SameUserMultipleTokens_DeletesOnlyTarget() {
        // given
        String username = "user@test.com";
        String oldToken = "old-token-to-delete";
        String newToken = "new-token-to-keep";

        PreUserRefreshEntity oldRefresh =
                PreUserRefreshEntity.builder().username(username).refresh(oldToken).build();

        PreUserRefreshEntity newRefresh =
                PreUserRefreshEntity.builder().username(username).refresh(newToken).build();

        preUserRefreshRepository.save(oldRefresh);
        preUserRefreshRepository.save(newRefresh);

        // when
        preUserRefreshRepository.deleteByRefresh(oldToken);

        // then
        assertThat(preUserRefreshRepository.existsByRefresh(oldToken)).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh(newToken)).isTrue();
    }

    @Test
    @DisplayName("삭제 후 전체 개수가 감소한다")
    void deleteByRefresh_DecreasesTotalCount() {
        // given
        String token1 = "token-1";
        String token2 = "token-2";
        String token3 = "token-3";

        PreUserRefreshEntity refresh1 =
                PreUserRefreshEntity.builder().username("user1@test.com").refresh(token1).build();

        PreUserRefreshEntity refresh2 =
                PreUserRefreshEntity.builder().username("user2@test.com").refresh(token2).build();

        PreUserRefreshEntity refresh3 =
                PreUserRefreshEntity.builder().username("user3@test.com").refresh(token3).build();

        preUserRefreshRepository.save(refresh1);
        preUserRefreshRepository.save(refresh2);
        preUserRefreshRepository.save(refresh3);

        long initialCount = preUserRefreshRepository.count();

        // when
        preUserRefreshRepository.deleteByRefresh(token2);

        // then
        long afterCount = preUserRefreshRepository.count();
        assertThat(afterCount).isEqualTo(initialCount - 1);
    }

    @Test
    @DisplayName("모든 엔티티를 삭제하면 데이터베이스가 비게 된다")
    void deleteByRefresh_DeleteAllTokens_EmptyDatabase() {
        // given
        String token1 = "token-1";
        String token2 = "token-2";

        PreUserRefreshEntity refresh1 =
                PreUserRefreshEntity.builder().username("user1@test.com").refresh(token1).build();

        PreUserRefreshEntity refresh2 =
                PreUserRefreshEntity.builder().username("user2@test.com").refresh(token2).build();

        preUserRefreshRepository.save(refresh1);
        preUserRefreshRepository.save(refresh2);

        // when
        preUserRefreshRepository.deleteByRefresh(token1);
        preUserRefreshRepository.deleteByRefresh(token2);

        // then
        assertThat(preUserRefreshRepository.count()).isEqualTo(0);
        assertThat(preUserRefreshRepository.existsByRefresh(token1)).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh(token2)).isFalse();
    }

    @Test
    @DisplayName("username으로 해당 사용자의 모든 refresh token을 삭제할 수 있다")
    void deleteByUsername_ExistingUser_Success() {
        // given
        String username = "user@test.com";
        String token = "refresh-token";
        PreUserRefreshEntity refreshEntity =
                PreUserRefreshEntity.builder().username(username).refresh(token).build();
        preUserRefreshRepository.save(refreshEntity);

        // when
        preUserRefreshRepository.deleteByUsername(username);

        // then
        Boolean exists = preUserRefreshRepository.existsByRefresh(token);
        assertThat(exists).isFalse();
        assertThat(preUserRefreshRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("존재하지 않는 username으로 삭제해도 에러가 발생하지 않는다")
    void deleteByUsername_NonExistingUser_NoError() {
        // given
        String nonExistingUsername = "nonexisting@test.com";

        // when & then - 에러 없이 정상 실행되어야 함
        preUserRefreshRepository.deleteByUsername(nonExistingUsername);
    }

    @Test
    @DisplayName("특정 사용자의 token만 삭제하고 다른 사용자의 token은 유지된다")
    void deleteByUsername_MultipleUsers_DeletesOnlyTargetUser() {
        // given
        String userToDelete = "delete@test.com";
        String userToKeep1 = "keep1@test.com";
        String userToKeep2 = "keep2@test.com";

        PreUserRefreshEntity refresh1 =
                PreUserRefreshEntity.builder()
                        .username(userToDelete)
                        .refresh("token-to-delete")
                        .build();

        PreUserRefreshEntity refresh2 =
                PreUserRefreshEntity.builder()
                        .username(userToKeep1)
                        .refresh("token-to-keep-1")
                        .build();

        PreUserRefreshEntity refresh3 =
                PreUserRefreshEntity.builder()
                        .username(userToKeep2)
                        .refresh("token-to-keep-2")
                        .build();

        preUserRefreshRepository.save(refresh1);
        preUserRefreshRepository.save(refresh2);
        preUserRefreshRepository.save(refresh3);

        // when
        preUserRefreshRepository.deleteByUsername(userToDelete);

        // then
        assertThat(preUserRefreshRepository.existsByRefresh("token-to-delete")).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh("token-to-keep-1")).isTrue();
        assertThat(preUserRefreshRepository.existsByRefresh("token-to-keep-2")).isTrue();
        assertThat(preUserRefreshRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("같은 username의 여러 refresh token이 모두 삭제된다")
    void deleteByUsername_MultipleTokensSameUser_DeletesAll() {
        // given
        String username = "user@test.com";
        String token1 = "token-1";
        String token2 = "token-2";
        String token3 = "token-3";

        PreUserRefreshEntity refresh1 =
                PreUserRefreshEntity.builder().username(username).refresh(token1).build();

        PreUserRefreshEntity refresh2 =
                PreUserRefreshEntity.builder().username(username).refresh(token2).build();

        PreUserRefreshEntity refresh3 =
                PreUserRefreshEntity.builder().username(username).refresh(token3).build();

        preUserRefreshRepository.save(refresh1);
        preUserRefreshRepository.save(refresh2);
        preUserRefreshRepository.save(refresh3);

        // when
        preUserRefreshRepository.deleteByUsername(username);

        // then
        assertThat(preUserRefreshRepository.existsByRefresh(token1)).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh(token2)).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh(token3)).isFalse();
        assertThat(preUserRefreshRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("같은 username의 여러 token을 삭제할 때 다른 사용자는 영향받지 않는다")
    void deleteByUsername_MultipleTokensSameUser_OtherUsersUnaffected() {
        // given
        String userToDelete = "delete@test.com";
        String otherUser = "other@test.com";

        PreUserRefreshEntity refresh1 =
                PreUserRefreshEntity.builder()
                        .username(userToDelete)
                        .refresh("delete-token-1")
                        .build();

        PreUserRefreshEntity refresh2 =
                PreUserRefreshEntity.builder()
                        .username(userToDelete)
                        .refresh("delete-token-2")
                        .build();

        PreUserRefreshEntity refresh3 =
                PreUserRefreshEntity.builder().username(otherUser).refresh("keep-token").build();

        preUserRefreshRepository.save(refresh1);
        preUserRefreshRepository.save(refresh2);
        preUserRefreshRepository.save(refresh3);

        long initialCount = preUserRefreshRepository.count();

        // when
        preUserRefreshRepository.deleteByUsername(userToDelete);

        // then
        assertThat(preUserRefreshRepository.existsByRefresh("delete-token-1")).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh("delete-token-2")).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh("keep-token")).isTrue();
        assertThat(preUserRefreshRepository.count()).isEqualTo(initialCount - 2);
    }

    @Test
    @DisplayName("삭제 후 전체 개수가 올바르게 감소한다")
    void deleteByUsername_DecreasesTotalCount() {
        // given
        String user1 = "user1@test.com";
        String user2 = "user2@test.com";

        PreUserRefreshEntity refresh1 =
                PreUserRefreshEntity.builder().username(user1).refresh("token-1").build();

        PreUserRefreshEntity refresh2 =
                PreUserRefreshEntity.builder().username(user1).refresh("token-2").build();

        PreUserRefreshEntity refresh3 =
                PreUserRefreshEntity.builder().username(user2).refresh("token-3").build();

        preUserRefreshRepository.save(refresh1);
        preUserRefreshRepository.save(refresh2);
        preUserRefreshRepository.save(refresh3);

        long initialCount = preUserRefreshRepository.count();

        // when
        preUserRefreshRepository.deleteByUsername(user1);

        // then
        long afterCount = preUserRefreshRepository.count();
        assertThat(afterCount).isEqualTo(initialCount - 2);
        assertThat(afterCount).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 시간 이전에 생성된 refresh token을 삭제할 수 있다")
    void deleteByCreatedDateBefore_DeletesOldTokens() throws InterruptedException {
        // given
        PreUserRefreshEntity oldRefresh1 =
                PreUserRefreshEntity.builder()
                        .username("user1@test.com")
                        .refresh("old-token-1")
                        .build();

        PreUserRefreshEntity oldRefresh2 =
                PreUserRefreshEntity.builder()
                        .username("user2@test.com")
                        .refresh("old-token-2")
                        .build();

        preUserRefreshRepository.save(oldRefresh1);
        preUserRefreshRepository.save(oldRefresh2);
        preUserRefreshRepository.flush();

        // 시간 차이를 만들기 위해 약간 대기
        Thread.sleep(100);
        LocalDateTime cutoffTime = LocalDateTime.now();
        Thread.sleep(100);

        PreUserRefreshEntity newRefresh =
                PreUserRefreshEntity.builder()
                        .username("user3@test.com")
                        .refresh("new-token")
                        .build();

        preUserRefreshRepository.save(newRefresh);
        preUserRefreshRepository.flush();

        // when
        preUserRefreshRepository.deleteByCreatedDateBefore(cutoffTime);

        // then
        assertThat(preUserRefreshRepository.existsByRefresh("old-token-1")).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh("old-token-2")).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh("new-token")).isTrue();
        assertThat(preUserRefreshRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("모든 토큰보다 이전 시간으로 삭제하면 아무것도 삭제되지 않는다")
    void deleteByCreatedDateBefore_BeforeAllTokens_DeletesNothing() {
        // given
        PreUserRefreshEntity refresh1 =
                PreUserRefreshEntity.builder()
                        .username("user1@test.com")
                        .refresh("token-1")
                        .build();

        PreUserRefreshEntity refresh2 =
                PreUserRefreshEntity.builder()
                        .username("user2@test.com")
                        .refresh("token-2")
                        .build();

        preUserRefreshRepository.save(refresh1);
        preUserRefreshRepository.save(refresh2);

        LocalDateTime veryOldTime = LocalDateTime.now().minusDays(30);

        // when
        preUserRefreshRepository.deleteByCreatedDateBefore(veryOldTime);

        // then
        assertThat(preUserRefreshRepository.count()).isEqualTo(2);
        assertThat(preUserRefreshRepository.existsByRefresh("token-1")).isTrue();
        assertThat(preUserRefreshRepository.existsByRefresh("token-2")).isTrue();
    }

    @Test
    @DisplayName("모든 토큰보다 이후 시간으로 삭제하면 모든 토큰이 삭제된다")
    void deleteByCreatedDateBefore_AfterAllTokens_DeletesAll() {
        // given
        PreUserRefreshEntity refresh1 =
                PreUserRefreshEntity.builder()
                        .username("user1@test.com")
                        .refresh("token-1")
                        .build();

        PreUserRefreshEntity refresh2 =
                PreUserRefreshEntity.builder()
                        .username("user2@test.com")
                        .refresh("token-2")
                        .build();

        preUserRefreshRepository.save(refresh1);
        preUserRefreshRepository.save(refresh2);
        preUserRefreshRepository.flush();

        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);

        // when
        preUserRefreshRepository.deleteByCreatedDateBefore(futureTime);

        // then
        assertThat(preUserRefreshRepository.count()).isEqualTo(0);
        assertThat(preUserRefreshRepository.existsByRefresh("token-1")).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh("token-2")).isFalse();
    }

    @Test
    @DisplayName("여러 사용자의 토큰 중 오래된 것만 삭제된다")
    void deleteByCreatedDateBefore_MultipleUsers_DeletesOnlyOldOnes() throws InterruptedException {
        // given
        PreUserRefreshEntity oldRefresh1 =
                PreUserRefreshEntity.builder()
                        .username("user1@test.com")
                        .refresh("old-token-1")
                        .build();

        PreUserRefreshEntity oldRefresh2 =
                PreUserRefreshEntity.builder()
                        .username("user1@test.com")
                        .refresh("old-token-2")
                        .build();

        preUserRefreshRepository.save(oldRefresh1);
        preUserRefreshRepository.save(oldRefresh2);
        preUserRefreshRepository.flush();

        Thread.sleep(100);
        LocalDateTime cutoffTime = LocalDateTime.now();
        Thread.sleep(100);

        PreUserRefreshEntity newRefresh1 =
                PreUserRefreshEntity.builder()
                        .username("user2@test.com")
                        .refresh("new-token-1")
                        .build();

        PreUserRefreshEntity newRefresh2 =
                PreUserRefreshEntity.builder()
                        .username("user3@test.com")
                        .refresh("new-token-2")
                        .build();

        preUserRefreshRepository.save(newRefresh1);
        preUserRefreshRepository.save(newRefresh2);
        preUserRefreshRepository.flush();

        // when
        preUserRefreshRepository.deleteByCreatedDateBefore(cutoffTime);

        // then
        assertThat(preUserRefreshRepository.count()).isEqualTo(2);
        assertThat(preUserRefreshRepository.existsByRefresh("old-token-1")).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh("old-token-2")).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh("new-token-1")).isTrue();
        assertThat(preUserRefreshRepository.existsByRefresh("new-token-2")).isTrue();
    }

    @Test
    @DisplayName("빈 데이터베이스에서 deleteByCreatedDateBefore를 호출해도 에러가 발생하지 않는다")
    void deleteByCreatedDateBefore_EmptyDatabase_NoError() {
        // given - setUp()에서 이미 deleteAll() 호출됨
        LocalDateTime anyTime = LocalDateTime.now();

        // when & then - 에러 없이 정상 실행되어야 함
        preUserRefreshRepository.deleteByCreatedDateBefore(anyTime);
        assertThat(preUserRefreshRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("같은 사용자의 여러 토큰 중 오래된 것만 삭제된다")
    void deleteByCreatedDateBefore_SameUserMultipleTokens_DeletesOnlyOldOnes()
            throws InterruptedException {
        // given
        String username = "user@test.com";

        PreUserRefreshEntity oldToken1 =
                PreUserRefreshEntity.builder().username(username).refresh("old-token-1").build();

        PreUserRefreshEntity oldToken2 =
                PreUserRefreshEntity.builder().username(username).refresh("old-token-2").build();

        preUserRefreshRepository.save(oldToken1);
        preUserRefreshRepository.save(oldToken2);
        preUserRefreshRepository.flush();

        Thread.sleep(100);
        LocalDateTime cutoffTime = LocalDateTime.now();
        Thread.sleep(100);

        PreUserRefreshEntity newToken =
                PreUserRefreshEntity.builder().username(username).refresh("new-token").build();

        preUserRefreshRepository.save(newToken);
        preUserRefreshRepository.flush();

        // when
        preUserRefreshRepository.deleteByCreatedDateBefore(cutoffTime);

        // then
        assertThat(preUserRefreshRepository.count()).isEqualTo(1);
        assertThat(preUserRefreshRepository.existsByRefresh("old-token-1")).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh("old-token-2")).isFalse();
        assertThat(preUserRefreshRepository.existsByRefresh("new-token")).isTrue();
    }
}
