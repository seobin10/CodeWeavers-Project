package com.cw.cwu.dto;

import com.cw.cwu.domain.Grade;
import com.cw.cwu.domain.StudentGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GradeDTO {
    private String studentId;
    private String courseName;
    private Integer credit;
    private String grade;

    // DB 저장용 변환
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

    // DB -> ENUM
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
