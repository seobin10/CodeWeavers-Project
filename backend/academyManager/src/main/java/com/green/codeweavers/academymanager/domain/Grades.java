package com.green.codeweavers.academymanager.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="grades")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Grades {
    // 성적 데이터에 대한 기능, 비즈니로직을 정의하는 영역
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long gradeId;
    @Enumerated(EnumType.STRING)
    private StudentGrade studentGrade;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollmentId;

}

