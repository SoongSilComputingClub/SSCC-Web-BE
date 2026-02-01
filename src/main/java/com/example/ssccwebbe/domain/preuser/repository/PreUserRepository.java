package com.example.ssccwebbe.domain.preuser.repository;

import com.example.ssccwebbe.domain.preuser.entity.PreUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreUserRepository extends JpaRepository<PreUserEntity,Long> {

    Optional<PreUserEntity> findByUsernameAndIsSocial(String username, Boolean social);
    Optional<PreUserEntity> findByUsernameAndIsLock(String username, Boolean isLock);

}
