package com.cw.cwu.dto;

import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.domain.StudentGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradeDTO {
    private Integer courseId;
    private String courseName;
    private Integer credit;
    private String grade;

    private Double gradePoint;   // 평점 점수 (4.5, 4.0 등)
    private String courseType;   // 전공/교양
    private String semester;     // 2024-1 형태

    // 기존 변환 메서드 유지
    public String ConvertToDb(StudentGrade grade) {
        return switch (grade) {
            case A_PLUS -> "A+";
            case B_PLUS -> "B+";
            case C_PLUS -> "C+";
            case D_PLUS -> "D+";
            case A0 -> "A";
            case B0 -> "B";
            case C0 -> "C";
            case D0 -> "D";
            default -> grade.name();
        };
    }

    public StudentGrade convertToEnum(String db) {
        return switch (db) {
            case "A+" -> StudentGrade.A_PLUS;
            case "B+" -> StudentGrade.B_PLUS;
            case "C+" -> StudentGrade.C_PLUS;
            case "D+" -> StudentGrade.D_PLUS;
            case "A" -> StudentGrade.A0;
            case "B" -> StudentGrade.B0;
            case "C" -> StudentGrade.C0;
            case "D" -> StudentGrade.D0;
            default -> StudentGrade.valueOf(db);
        };
    }

}