package com.cw.cwu.service.professor;


import com.cw.cwu.dto.GradeDetailDTO;
import com.cw.cwu.dto.GradeRegisterDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import org.springframework.security.access.AccessDeniedException;


public interface ProfessorGradeService {

    /**
     * [성적 등록]
     * @param dto 성적 등록 요청 DTO
     * @param professorId 요청 교수 ID (권한 검증용)
     * @return 등록 결과 메시지
     * @throws AccessDeniedException 권한 없음
     */
    String registerGrade(GradeRegisterDTO dto, String professorId) throws AccessDeniedException;

    /**
     * [성적 수정]
     * @param dto 성적 수정 요청 DTO
     * @param professorId 요청 교수 ID (권한 검증용)
     * @return 수정 결과 메시지
     * @throws AccessDeniedException 권한 없음
     */
    String updateGrade(GradeRegisterDTO dto, String professorId) throws AccessDeniedException;

    /**
     * [성적 삭제]
     * @param gradeId 삭제할 성적 ID
     * @param professorId 요청 교수 ID (권한 검증용)
     * @return 삭제 결과 메시지
     * @throws AccessDeniedException 권한 없음
     */
    String deleteGrade(Integer gradeId, String professorId) throws AccessDeniedException;

    /**
     * [강의별 성적 조회]
     * @param professorId 요청 교수 ID (권한 검증용)
     * @param classId 강의 ID
     * @param pageRequestDTO 페이징 및 정렬 정보
     * @return 성적 상세 목록 DTO 페이징 응답
     * @throws AccessDeniedException 권한 없음
     */
    PageResponseDTO<GradeDetailDTO> getGradesByClass(String professorId, Integer classId, PageRequestDTO pageRequestDTO) throws AccessDeniedException;
}
