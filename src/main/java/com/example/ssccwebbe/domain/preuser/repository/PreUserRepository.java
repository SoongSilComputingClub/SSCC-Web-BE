package com.example.ssccwebbe.domain.preuser.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ssccwebbe.domain.preuser.entity.PreUserEntity;

public interface PreUserRepository extends JpaRepository<PreUserEntity, Long> {
    void deleteByCreatedDateBefore(LocalDateTime createdDateBefore);
}
