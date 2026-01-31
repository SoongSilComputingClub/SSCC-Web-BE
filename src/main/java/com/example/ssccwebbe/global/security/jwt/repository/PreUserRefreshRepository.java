package com.example.ssccwebbe.global.security.jwt.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ssccwebbe.global.security.jwt.entity.PreUserRefreshEntity;

public interface PreUserRefreshRepository extends JpaRepository<PreUserRefreshEntity, Long> {
    void deleteByCreatedDateBefore(LocalDateTime createdDateBefore);
}
