package com.example.ssccwebbe.domain.applyform.entity;

import java.time.LocalDateTime;

import com.example.ssccwebbe.domain.preuser.entity.PreUserEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(
	name = "applyForm",
	uniqueConstraints = @UniqueConstraint(name = "uk_applyForm_preuser", columnNames = "preuser_id")
)
public class ApplyFormEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ERD: preuser_id
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "preuser_id", nullable = false)
	private PreUserEntity preUser;

	@Column(name = "applicant_name", nullable = false)
	private String applicantName;

	@Column(nullable = false)
	private String department;

	@Column(name = "student_no", nullable = false)
	private String studentNo;

	// ERD에 grade INTEGER
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

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public static ApplyFormEntity create(PreUserEntity preUser) {
		ApplyFormEntity e = new ApplyFormEntity();
		ee.preUser = preUser;
		e.status = "SUBMITTED";
		return e;
	}

	public void update(
		String applicantName,
		String department,
		String studentNo,
		Integer grade,
		String phone,
		String gender,
		String introduce,
		String codingLevel,
		String techStackText
	) {
		this.applicantName = applicantName;
		this.department = department;
		this.studentNo = studentNo;
		this.grade = grade;
		this.phone = phone;
		this.gender = gender;
		this.introduce = introduce;
		this.codingLevel = codingLevel;
		this.techStackText = techStackText;
	}

	@PrePersist
	public void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
		if (this.status == null) this.status = "SUBMITTED";
	}

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}