package com.cw.cwu.service;

import com.cw.cwu.domain.Grade;
import com.cw.cwu.domain.StudentGrade;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.GradeDTO;
import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.repository.GradeRepository;
import com.cw.cwu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    public List<GradeDTO> getStudentGrade(String studentId) {
        return gradeRepository.findGrade(studentId)
        .stream()
        .map(grade -> new GradeDTO(
                grade.getStudentId(),
                grade.getCourseName(),
                grade.getCredit(),
                grade.ConvertToDb(StudentGrade.valueOf(grade.getGrade()))
        ))
        .collect(Collectors.toList());
    }
}
