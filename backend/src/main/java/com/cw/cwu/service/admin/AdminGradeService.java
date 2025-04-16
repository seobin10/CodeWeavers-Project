package com.cw.cwu.service.admin;

import com.cw.cwu.dto.GradeStatusDTO;
import com.cw.cwu.dto.SemesterDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdminGradeService {

    /**
     * 특정 학기의 모든 학생에 대해 GPA 집계를 수행합니다.
     * 성적 입력 마감 이후, 관리자에 의해 수동 트리거됩니다.
     *
     * @param semesterId 집계 대상 학기 ID
     */
    void finalizeStudentRecordsByDepartment(Integer semesterId, Integer departmentId);

    /**
     * 특정 학생의 한 학기 성적 데이터를 기반으로
     * GPA, 수강 학점, 이수 학점을 계산하여 student_records 테이블에 저장하는 메서드
     *
     * - 성적이 하나라도 미입력된 경우 IllegalStateException 예외 발생
     * - @Transactional: 하나라도 실패 시 전체 롤백
     *
     * @param studentId   대상 학생 ID
     * @param semesterId  집계 대상 학기의 ID
     */
    void updateStudentRecordAsAdmin(String studentId, Integer semesterId);


    List<GradeStatusDTO> getGradeStatusSummary(Integer semesterId, Integer departmentId);


    SemesterDTO getCurrentSemesterDTO();

}
