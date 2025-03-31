package com.cw.cwu.service.student;

import com.cw.cwu.dto.*;

import java.util.List;
import java.util.Map;

public interface StudentEnrollmentService {

    /**
     * [수강 신청 가능한 강의 목록 조회 (페이징 + 필터)]
     * @param studentId 학생 ID
     * @param courseType 강의 구분 (전공/교양)
     * @param departmentName 학과명
     * @param courseYear 학년
     * @param classDay 요일
     * @param classStart 시작 교시
     * @param credit 학점
     * @param courseName 과목명
     * @param pageRequestDTO 페이징 정보
     * @return 강의 목록 Map 페이징 응답
     */
    PageResponseDTO<Map<String, Object>> getAvailableCoursesPaged(
            String studentId,
            String courseType,
            String departmentName,
            Integer courseYear,
            String classDay,
            Integer classStart,
            Integer credit,
            String courseName,
            PageRequestDTO pageRequestDTO
    );

    /**
     * [수강 신청 가능한 학과 목록 조회]
     * @return 학과 이름 리스트 (Map 형태)
     */
    List<Map<String, Object>> getDepartments();

    /**
     * [수강 신청 가능한 강의 구분 목록 조회]
     * @return 전공/교양 리스트 (Map 형태)
     */
    List<Map<String, Object>> getCourseTypes();

    /**
     * [수강 신청 가능한 강의 학년 목록 조회]
     * @return 학년 리스트 (Map 형태)
     */
    List<Map<String, Object>> getCourseYears();

    /**
     * [수강 신청 가능한 요일 목록 조회]
     * @return 요일 리스트 (Map 형태)
     */
    List<Map<String, Object>> getClassDays();

    /**
     * [수강 신청 가능한 시간대 목록 조회]
     * @return 시작 교시 리스트 (Map 형태)
     */
    List<Map<String, Object>> getClassTimes();

    /**
     * [수강 신청 가능한 학점 목록 조회]
     * @return 학점 리스트 (Map 형태)
     */
    List<Map<String, Object>> getCredits();

    /**
     * [내가 신청한 강의 목록 조회]
     * @param studentId 학생 ID
     * @return 수강 신청된 강의 정보 리스트
     */
    List<Map<String, Object>> getMyCourses(String studentId);

    /**
     * [수강 신청 삭제]
     * @param studentId 학생 ID
     * @param classId 강의 ID
     * @return 성공/실패 메시지
     */
    String deleteCourse(String studentId, Integer classId);

    /**
     * [수강 확정된 강의 목록 조회]
     * 시간표나 최종 수강 목록 출력용
     * @param studentId 학생 ID
     * @return 확정된 강의 정보 리스트
     */
    List<Map<String, Object>> getConfirmedCourses(String studentId);

    /**
     * [수강 신청]
     * 다양한 조건(중복, 정원, 학점, 시간 충돌 등)을 검증 후 수강 신청을 수행
     * @param requestDTO 수강 신청 요청 DTO
     * @return 성공/실패 메시지
     */
    String applyToClass(EnrollmentRequestDTO requestDTO);
}