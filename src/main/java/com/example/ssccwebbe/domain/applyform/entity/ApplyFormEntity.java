package com.example.ssccwebbe.domain.applyform.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.ssccwebbe.domain.applyform.dto.ApplyFormCreateOrUpdateRequest;
import com.example.ssccwebbe.domain.preuser.entity.PreUserEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 지원서 엔티
// 지원상태는 submitted로 시작, 추후 운영진이 합/불에 따라 변경되게

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "apply_form_entity",
        uniqueConstraints =
                @UniqueConstraint(name = "uk_apply_form_preuser", columnNames = "preuser_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplyFormEntity {

    private static final String STATUS_SUBMITTED = "SUBMITTED";
    private static final String STATUS_DELETED = "DELETED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // PreUser 1명당 지원서 1개
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "preuser_id", nullable = false, updatable = false)
    private PreUserEntity preUser;

    @Column(name = "applicant_name", nullable = false)
    private String applicantName;

    @Column(nullable = false)
    private String department;

    @Column(name = "student_no", nullable = false)
    private String studentNo;

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false, length = 2000)
    private String introduce;

    @Column(name = "coding_level", nullable = false)
    private String codingLevel;

    @Column(name = "tech_stack_text", nullable = false, length = 2000)
    private String techStackText;

    @Column(nullable = false)
    private String status;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public static ApplyFormEntity create(
            PreUserEntity preUser, ApplyFormCreateOrUpdateRequest req) {
        return new ApplyFormEntity(
                null,
                preUser,
                req.applicantName(),
                req.department(),
                req.studentNo(),
                req.grade(),
                req.phone(),
                req.gender(),
                req.introduce(),
                req.codingLevel(),
                req.techStackText(),
                "SUBMITTED",
                null,
                null);
    }

    // 지원서 수정 용

    public void update(ApplyFormCreateOrUpdateRequest req) {
        this.applicantName = req.applicantName();
        this.department = req.department();
        this.studentNo = req.studentNo();
        this.grade = req.grade();
        this.phone = req.phone();
        this.gender = req.gender();
        this.introduce = req.introduce();
        this.codingLevel = req.codingLevel();
        this.techStackText = req.techStackText();
    }

    // 소프트 delete
    public void softDelete() {
        this.status = STATUS_DELETED;
    }

    // 소프트 delete된 지원서 복구 (재작성 시)
    public void restoreSubmitted() {
        this.status = STATUS_SUBMITTED;
    }

    // 소프트 delete 여부
    public boolean isDeleted() {
        return STATUS_DELETED.equals(this.status);
    }
}
