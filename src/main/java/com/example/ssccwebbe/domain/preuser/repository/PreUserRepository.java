package com.example.ssccwebbe.domain.preuser.repository;

import com.example.ssccwebbe.domain.preuser.entity.PreUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PreUserRepository extends JpaRepository<PreUserEntity, Long> {
    void deleteByCreatedDateBefore(LocalDateTime createdDateBefore);
}
