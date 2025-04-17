package com.cw.cwu.service.student;

import com.cw.cwu.domain.StudentGrade;
import com.cw.cwu.domain.StudentRecord;
import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.dto.TotalRecordDTO;

import java.util.List;

public interface StudentGradeService {
    List<GradeDTO> getStudentGrade(String studentId);

    StudentRecord getStudentRecord(String studentId);

    // 선택 학기 성적 조회
    List<GradeDTO> getStudentGradeBySemester(String studentId, Integer semesterId);

    List<StudentRecord> getAllStudentRecords(String studentId);

    TotalRecordDTO getTotalRecord(String studentId);
    // 학생 성적 기록 업데이트 (학점, 취득 학점, GPA 계산 후 저장)
}