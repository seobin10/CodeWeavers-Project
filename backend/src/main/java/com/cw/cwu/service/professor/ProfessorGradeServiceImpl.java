package com.cw.cwu.service.professor;

import com.cw.cwu.domain.ClassEntity;
import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.domain.Grade;
import com.cw.cwu.dto.GradeDetailDTO;
import com.cw.cwu.dto.GradeRegisterDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import com.cw.cwu.repository.ClassEntityRepository;
import com.cw.cwu.repository.EnrollmentRepository;
import com.cw.cwu.repository.GradeRepository;
import com.cw.cwu.util.AuthUtil;
import com.cw.cwu.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class ProfessorGradeServiceImpl implements ProfessorGradeService {

    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepository;
    private final ClassEntityRepository classEntityRepository;

    @Override
    public String registerGrade(GradeRegisterDTO dto, String professorId) throws AccessDeniedException {
        Enrollment enrollment = enrollmentRepository.findById(dto.getEnrollmentId()).orElse(null);
        if (enrollment == null) return "수강 정보가 존재하지 않습니다.";

        // 권한 검사
        AuthUtil.checkOwnership(enrollment.getEnrolledClassEntity().getProfessor().getUserId(), professorId);

        if (enrollment.getGrade() != null) return "이미 성적 등록된 수강입니다.";

        Grade grade = Grade.builder()
                .enrollment(enrollment)
                .grade(dto.getGrade())
                .build();

        gradeRepository.save(grade);
        return "성적 등록 완료";

    }

    @Transactional
    @Override
    public String updateGrade(GradeRegisterDTO dto, String professorId) throws AccessDeniedException {
        Enrollment enrollment = enrollmentRepository.findById(dto.getEnrollmentId()).orElse(null);
        if (enrollment == null || enrollment.getGrade() == null) return "등록된 성적이 없습니다.";

        AuthUtil.checkOwnership(enrollment.getEnrolledClassEntity().getProfessor().getUserId(), professorId);

        Grade grade = enrollment.getGrade();
        grade.setGrade(dto.getGrade());

        gradeRepository.save(grade);
        return "성적 수정 완료";
    }

    @Transactional
    @Override
    public String deleteGrade(Integer gradeId, String professorId) throws AccessDeniedException {
        Grade grade = gradeRepository.findById(gradeId).orElse(null);
        if (grade == null) return "성적 정보가 없습니다.";

        Enrollment enrollment = grade.getEnrollment();
        if (enrollment == null) return "수강 정보가 유효하지 않습니다.";

        AuthUtil.checkOwnership(enrollment.getEnrolledClassEntity().getProfessor().getUserId(), professorId);

        enrollment.setGrade(null);
        enrollmentRepository.save(enrollment);
        gradeRepository.delete(grade);
        return "성적 삭제 완료";
    }

    @Override
    public PageResponseDTO<GradeDetailDTO> getGradesByClass(String professorId, Integer classId, PageRequestDTO pageRequestDTO) throws AccessDeniedException {
        ClassEntity classEntity = classEntityRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        AuthUtil.checkOwnership(classEntity.getProfessor().getUserId(), professorId);

        Pageable pageable = PageUtil.toPageable(pageRequestDTO, "enrollment");
        Page<Enrollment> page = enrollmentRepository.findByEnrolledClassEntity_Id(classId, pageable);

        Page<GradeDetailDTO> dtoPage = page.map(e -> {
            Grade grade = e.getGrade();
            return new GradeDetailDTO(
                    e.getStudent().getUserId(),
                    e.getStudent().getUserName(),
                    e.getEnrolledClassEntity().getCourse().getName(),
                    e.getEnrolledClassEntity().getCourse().getCredit(),
                    grade != null ? grade.getGrade().name() : null,
                    e.getEnrollment(),
                    grade != null ? grade.getGradeId() : null
            );
        });

        return PageUtil.toDTO(dtoPage, pageRequestDTO.getPage());
    }
}
