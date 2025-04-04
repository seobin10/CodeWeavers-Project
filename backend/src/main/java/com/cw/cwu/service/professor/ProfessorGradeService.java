package com.cw.cwu.service.professor;


import com.cw.cwu.dto.GradeDetailDTO;
import com.cw.cwu.dto.GradeRegisterDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ProfessorGradeService {
    String registerGrade(GradeRegisterDTO dto) throws AccessDeniedException;
    String updateGrade(GradeRegisterDTO dto);
    String deleteGrade(Integer gradeId);
    public PageResponseDTO<GradeDetailDTO> getGradesByClass(String professorId, Integer classId, PageRequestDTO pageRequestDTO) throws AccessDeniedException;

}
