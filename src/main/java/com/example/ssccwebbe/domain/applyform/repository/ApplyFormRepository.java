package com.example.ssccwebbe.domain.applyform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ssccwebbe.domain.applyform.entity.ApplyFormEntity;
import com.example.ssccwebbe.domain.preuser.entity.PreUserEntity;

public interface ApplyFormRepository extends JpaRepository<ApplyFormEntity, Long> {
	Optional<ApplyFormEntity> findByPreUser(PreUserEntity preUser);
	boolean existsByPreUser(PreUserEntity preUser);
}