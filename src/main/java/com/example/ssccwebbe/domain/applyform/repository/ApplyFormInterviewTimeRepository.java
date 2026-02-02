package com.example.ssccwebbe.domain.applyform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ssccwebbe.domain.applyform.entity.ApplyFormEntity;
import com.example.ssccwebbe.domain.applyform.entity.ApplyFormInterviewTimeEntity;

public interface ApplyFormInterviewTimeRepository extends JpaRepository<ApplyFormInterviewTimeEntity, Long> {

	List<ApplyFormInterviewTimeEntity> findAllByApplyFormOrderByInterviewDateAscStartTimeAsc(ApplyFormEntity applyForm);

	void deleteAllByApplyForm(ApplyFormEntity applyForm);
}