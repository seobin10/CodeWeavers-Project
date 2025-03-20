package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer classId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;  // 객체 매핑 (courseId → course)

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private User professor; // 객체 매핑 (professorId → professor)

    @Column(name = "class_semester", nullable = false)
    private String classSemester;

    @Column(name = "class_day", nullable = false)
    private String classDay;  // 강의 요일 (월, 화, 수, 목, 금)

    @Column(name = "class_start", nullable = false)
    private Integer classStart;  // 강의 시작 교시

    @Column(name = "class_end", nullable = false)
    private Integer classEnd;  // 강의 종료 교시

    @Column(name = "class_capacity", nullable = false)
    private Integer classCapacity;  // 강의 정원

    @Column(name = "class_enrolled", nullable = false)
    private Integer classEnrolled;  // 현재 수강 신청된 학생 수
}
