package com.example.ssccwebbe.global.security.jwt.repository;

import java.time.LocalDateTime;

import com.example.ssccwebbe.global.security.jwt.entity.PreUserRefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreUserRefreshRepository extends JpaRepository<PreUserRefreshEntity, Long> {
    void deleteByCreatedDateBefore(LocalDateTime createdDateBefore);
}
