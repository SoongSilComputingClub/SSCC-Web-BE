package com.example.ssccwebbe.domain.preuser.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ssccwebbe.domain.preuser.entity.PreUserEntity;

public interface PreUserRepository extends JpaRepository<PreUserEntity, Long> {

    Optional<PreUserEntity> findByUsernameAndIsSocial(String username, Boolean social);

    Optional<PreUserEntity> findByUsernameAndIsLock(String username, Boolean isLock);
}
