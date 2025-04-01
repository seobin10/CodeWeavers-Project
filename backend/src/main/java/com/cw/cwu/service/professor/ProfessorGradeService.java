package com.cw.cwu.service.professor;


import com.cw.cwu.dto.GradeDetailDTO;
import com.cw.cwu.dto.GradeRegisterDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;

import java.util.List;

public interface ProfessorGradeService {
    String registerGrade(GradeRegisterDTO dto);
    String updateGrade(GradeRegisterDTO dto);
    String deleteGrade(Integer gradeId);
    PageResponseDTO<GradeDetailDTO> getGradesByClass(Integer classId, PageRequestDTO pageRequestDTO);
}
