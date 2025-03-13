package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @Column(name = "grade_id")
    private Long gradeId;

    @Column(name = "enrollment_id")
    private Long enrollmentId;

    @Column(name = "grade_grade")
    private StudentGrade grade;

    // DB 저장용 변환
    private String ConvertToDb(StudentGrade grade) {
        return switch (grade) {
            case A_PLUS -> "A+";
            case B_PLUS -> "B+";
            case C_PLUS -> "C+";
            case D_PLUS -> "D+";
            default -> grade.name();
        };
    }

    // DB -> ENUM
    private StudentGrade convertToEnum(String db) {
        return switch (db) {
            case "A+" -> StudentGrade.A_PLUS;
            case "B+" -> StudentGrade.B_PLUS;
            case "C+" -> StudentGrade.C_PLUS;
            case "D+" -> StudentGrade.D_PLUS;
            default -> StudentGrade.valueOf(db);
        };
    }



}
