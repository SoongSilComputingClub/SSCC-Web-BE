package com.example.ssccwebbe.domain.preuser.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.ssccwebbe.domain.preuser.entity.PreUserRefreshEntity;

public interface PreUserRefreshRepository extends JpaRepository<PreUserRefreshEntity, Long> {
    Boolean existsByRefresh(String refreshToken);

    @Transactional
    void deleteByRefresh(String refresh);

    @Transactional
    void deleteByUsername(String username);

    void deleteByCreatedDateBefore(LocalDateTime createdDateBefore);
}
