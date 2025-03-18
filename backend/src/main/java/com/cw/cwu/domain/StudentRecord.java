package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_records")
@Getter
@Setter
public class StudentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;  // 기록 ID (자동 증가)

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;  // 학생 ID (외래키 연결)

    @Column(name = "semester", nullable = false, length = 10)
    private String semester;  // 학기 정보 (예: "2024-1")

    @Column(name = "enrolled", nullable = false)
    private int enrolled;  // 수강 신청한 학점

    @Column(name = "earned", nullable = false)
    private int earned;  // 취득한 학점 (F 제외)

    @Column(name = "gpa", nullable = false, columnDefinition = "FLOAT")
    private float gpa;  // 평균 평점 (GPA) - FLOAT로 명확하게 설정

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 기록 생성 시간 (기본값: 현재 시간)
}