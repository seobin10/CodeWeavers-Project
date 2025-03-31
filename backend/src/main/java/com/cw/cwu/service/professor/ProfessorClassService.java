package com.cw.cwu.service.professor;

import com.cw.cwu.dto.*;

import java.util.List;

public interface ProfessorClassService {

    /**
     * [강의 생성]
     * @param dto 강의 생성 요청 DTO
     * @return 성공 메시지 또는 오류 메시지
     */
    String createClass(ClassCreateRequestDTO dto);

    /**
     * [강의 목록 조회 (페이징 처리)]
     * @param professorId 교수 ID
     * @param pageRequestDTO 페이징 및 정렬 정보
     * @return 강의 목록 DTO 페이징 응답
     */
    PageResponseDTO<ClassDTO> getMyClasses(String professorId, PageRequestDTO pageRequestDTO);

    /**
     * [강의 정보 수정]
     * @param dto 강의 수정 요청 DTO
     * @return 성공 또는 실패 메시지
     */
    String updateClass(ClassUpdateRequestDTO dto);

    /**
     * [특정 강의 삭제]
     * @param classId 삭제할 강의 ID
     * @return 성공 또는 실패 메시지
     */
    String deleteClass(Integer classId);

    /**
     * [전체 과목 목록 조회]
     * @return 과목 ID와 이름을 담은 CourseSimpleDTO 리스트
     */
    List<CourseSimpleDTO> getAllCourses();

    /**
     * [전체 강의실 목록 조회]
     * @return 강의실 ID, 이름, 건물명을 담은 LectureRoomSimpleDTO 리스트
     */
    List<LectureRoomSimpleDTO> getAllLectureRooms();
}