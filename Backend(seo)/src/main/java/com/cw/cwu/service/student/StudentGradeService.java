package com.cw.cwu.service.student;

import com.cw.cwu.domain.StudentGrade;
import com.cw.cwu.dto.GradeDTO;
import java.util.List;

public interface StudentGradeService {
    List<GradeDTO> getStudentGrade(String studentId);
    void updateStudentRecords(String studentId);
}