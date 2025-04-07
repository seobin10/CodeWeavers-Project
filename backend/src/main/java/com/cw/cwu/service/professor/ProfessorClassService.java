package com.cw.cwu.service.professor;

import com.cw.cwu.dto.*;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

public interface ProfessorClassService {

    /**
     * [강의 등록]
     * @param dto 강의 생성 요청 DTO
     * @return 등록 성공 시 "성공", 실패 시 상세 오류 메시지
     */
    String createClass(ClassCreateRequestDTO dto);

    /**
     * [교수 본인의 강의 목록 조회]
     * @param professorId 교수 ID
     * @param pageRequestDTO 페이징 정보
     * @return 강의 목록 DTO 페이징 응답
     */
    PageResponseDTO<ClassDTO> getMyClasses(String professorId, PageRequestDTO pageRequestDTO, Integer semesterId);

    /**
     * [강의 수정]
     * @param dto 수정할 강의 정보 DTO
     * @param professorId 요청자 교수 ID (JWT 기반)
     * @return 수정 성공 시 "수정 완료", 실패 시 오류 메시지
     * @throws AccessDeniedException 권한 없음
     */
    String updateClass(ClassUpdateRequestDTO dto, String professorId) throws AccessDeniedException;

    /**
     * [강의 삭제]
     * @param classId 삭제 대상 강의 ID
     * @param professorId 요청자 교수 ID
     * @return 삭제 성공 시 "삭제 완료", 실패 시 오류 메시지
     * @throws AccessDeniedException 권한 없음
     */
    String deleteClass(Integer classId, String professorId) throws AccessDeniedException;

    /**
     * [교수의 과목 목록 조회]
     * @param professorId 교수 ID
     * @return CourseSimpleDTO 리스트 (과목 ID, 이름, 타입)
     */
    List<CourseSimpleDTO> getCoursesByProfessor(String professorId);

    /**
     * [조건에 맞는 빈 강의실 목록 조회]
     * @param day 요일 ("월", "화" 등)
     * @param startTime 시작 교시
     * @param endTime 종료 교시
     * @return LectureRoomSimpleDTO 리스트
     */
    List<LectureRoomSimpleDTO> getAvailableLectureRooms(String day, int startTime, int endTime);
}
