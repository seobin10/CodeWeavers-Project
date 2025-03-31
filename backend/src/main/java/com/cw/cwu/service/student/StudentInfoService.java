package com.cw.cwu.service.student;

public interface StudentInfoService {

    /**
     * [학생 학년 계산]
     * 계산 기준:
     * - 0 ~ 23학점: 1학년
     * - 24 ~ 47학점: 2학년
     * - 48 ~ 71학점: 3학년
     * - 72학점 이상: 4학년
     *
     * @param studentId 학년을 계산할 학생 ID
     * @return 계산된 학년 (1~4)
     */
    int calculateStudentYear(String studentId);

    /**
     * [졸업 가능 여부 확인]
     * 누적 취득 학점이 130학점 이상인지 확인
     * @param studentId 졸업 여부를 확인할 학생 ID
     * @return 졸업 가능 여부 (true: 가능, false: 불가)
     */
    boolean checkGraduationEligibility(String studentId);
}