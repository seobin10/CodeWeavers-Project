package com.cw.cwu.service.professor;

import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.dto.GradeRegisterDTO;

import java.util.List;

public interface ProfessorGradeService {
    String registerGrade(GradeRegisterDTO dto);
    String updateGrade(GradeRegisterDTO dto);
    String deleteGrade(Integer gradeId);
    List<GradeDTO> getGradesByClass(Integer classId);
}
