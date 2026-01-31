package com.example.ssccwebbe.domain.preuser.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ssccwebbe.domain.preuser.entity.PreUserRefreshEntity;

public interface PreUserRefreshRepository extends JpaRepository<PreUserRefreshEntity, Long> {
    void deleteByCreatedDateBefore(LocalDateTime createdDateBefore);
}
