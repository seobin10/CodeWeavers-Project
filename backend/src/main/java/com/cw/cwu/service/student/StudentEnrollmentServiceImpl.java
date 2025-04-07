package com.cw.cwu.service.student;

import com.cw.cwu.domain.*;
import com.cw.cwu.dto.EnrollmentRequestDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import com.cw.cwu.repository.ClassEntityRepository;
import com.cw.cwu.repository.EnrollmentRepository;
import com.cw.cwu.repository.UserRepository;
import com.cw.cwu.service.admin.AdminScheduleService;
import com.cw.cwu.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

    private final ModelMapper modelMapper;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final ClassEntityRepository classEntityRepository;
    private final StudentInfoService studentInfoService;
    private final AdminScheduleService adminScheduleService;

    // 학생이 수강 신청 가능한 강의 목록 조회
    @Override
    public PageResponseDTO<Map<String, Object>> getAvailableCoursesPaged(
            String studentId,
            String courseType,
            String departmentName,
            Integer courseYear,
            String classDay,
            Integer classStart,
            Integer credit,
            String courseName,
            PageRequestDTO pageRequestDTO
    ) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize()); // 0이 베이스이므로

        Page<Map<String, Object>> result = classEntityRepository.findAvailableCoursesPaged(
                studentId, courseType, departmentName, courseYear,
                classDay, classStart, credit, courseName, pageable
        );

        return PageUtil.toDTO(result, pageRequestDTO.getPage());
    }

    // 학과 목록 필터링
    @Override
    public List<Map<String, Object>> getDepartments() {
        return classEntityRepository.findDistinctDepartments().stream()
                .map(d -> Map.<String, Object>of("departmentName", d))
                .collect(Collectors.toList());
    }

    // 강의 구분 필터링
    @Override
    public List<Map<String, Object>> getCourseTypes() {
        return classEntityRepository.findDistinctCourseTypes().stream()
                .map(ct -> Map.<String, Object>of("courseType",
                        ct.equals("MAJOR") ? "전공" : "교양"))
                .collect(Collectors.toList());
    }

    // 강의 학년 필터링
    @Override
    public List<Map<String, Object>> getCourseYears() {
        return classEntityRepository.findDistinctCourseYears().stream()
                .map(y -> Map.<String, Object>of("courseYear", y))
                .collect(Collectors.toList());
    }

    // 강의 요일 필터링
    @Override
    public List<Map<String, Object>> getClassDays() {
        return classEntityRepository.findDistinctClassDays().stream()
                .map(cd -> Map.<String, Object>of("classDay", cd))
                .collect(Collectors.toList());
    }

    // 강의 시작 시간 필터링
    @Override
    public List<Map<String, Object>> getClassTimes() {
        return classEntityRepository.findDistinctClassTimes().stream()
                .map(ct -> Map.<String, Object>of("classTime", ct))
                .collect(Collectors.toList());
    }

    // 학점 목록 조회 필터링
    @Override
    public List<Map<String, Object>> getCredits() {
        return classEntityRepository.findDistinctCredits().stream()
                .map(c -> Map.<String, Object>of("credit", c))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getMyCourses(String studentId) {
        User student = userRepository.findByUserId(studentId).orElseThrow();
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        return enrollments.stream()
                .map(e -> {
                    ClassEntity c = e.getEnrolledClassEntity();
                    Map<String, Object> courseMap = new HashMap<>();
                    courseMap.put("강의번호", c.getId());
                    courseMap.put("강의명", c.getCourse().getName());
                    courseMap.put("강의요일", c.getDay());
                    courseMap.put("강의시간", c.getStartTime() + "~" + c.getEndTime());
                    courseMap.put("강의학점", c.getCourse().getCredit());
                    courseMap.put("담당교수", c.getProfessor() != null ? c.getProfessor().getName() : "미정");
                    return courseMap;
                }).collect(Collectors.toList());
    }

    @Override
    public String deleteCourse(String studentId, Integer classId) {
        User student = userRepository.findByUserId(studentId).orElseThrow();
        ClassEntity classEntity = classEntityRepository.findById(classId).orElseThrow();
        Optional<Enrollment> optional = enrollmentRepository.findByStudentAndEnrolledClassEntity(student, classEntity);
        if (optional.isPresent()) {
            enrollmentRepository.delete(optional.get());
            System.out.println("강의 삭제 완료: studentId=" + studentId + ", classId=" + classId);
            // 수강 삭제 시, 수강 인원 1 감소하도록 수정
            classEntity.setEnrolled(classEntity.getEnrolled() - 1);
            classEntityRepository.save(classEntity);
            return "수강 삭제되었습니다";
        }
        System.out.println("삭제 실패: 해당 강의 없음 - studentId=" + studentId + ", classId=" + classId);
        return "삭제할 강의를 찾을 수 없습니다.";
    }

    @Override
    public List<Map<String, Object>> getConfirmedCourses(String studentId) {
        User student = userRepository.findByUserId(studentId).orElseThrow();
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        return enrollments.stream()
                .filter(e -> e.getEnrolledClassEntity() != null) // Null 방지
                .map(e -> {
                    ClassEntity c = e.getEnrolledClassEntity();
                    Map<String, Object> courseMap = new HashMap<>();
                    courseMap.put("강의번호", c.getId());
                    courseMap.put("courseName", c.getCourse().getName());
                    courseMap.put("classDay", c.getDay());
                    courseMap.put("classRoom", c.getLectureRoom() != null ? c.getLectureRoom().getName() : "미정");
                    courseMap.put("professorName", c.getProfessor() != null ? c.getProfessor().getName() : "미정");
                    courseMap.put("classStartPeriod", c.getStartTime());
                    courseMap.put("classEndPeriod", c.getEndTime());
                    courseMap.put("classCredit", c.getCourse().getCredit());
                    return courseMap;
                }).collect(Collectors.toList());
    }

    // 수강 신청 처리
    @Override
    public String applyToClass(EnrollmentRequestDTO requestDTO) {
        // 수강신청 가능 기간인지 확인
        if (!adminScheduleService.isScheduleOpen(ScheduleType.ENROLL)) {
            return "현재는 수강신청 기간이 아닙니다!";
        }

        System.out.println("service applyToClass : " + requestDTO);
        User student = userRepository.getReferenceById(requestDTO.getStudentId());

        ClassEntity classEntity = classEntityRepository.getReferenceById(requestDTO.getClassId());

        // 중복 신청 방지
        if (enrollmentRepository.existsByStudentAndEnrolledClassEntity(student, classEntity)) {
            return "이미 신청한 강의입니다.";
        }

        // 정원 체크
        if (classEntity.getEnrolled() >= classEntity.getCapacity()) {
            return "수강 정원이 초과되었습니다!";
        }

        // 학과 제한 (교양과목 제외)
        if (classEntity.getCourse().getDepartment() != null &&
                !student.getDepartment().equals(classEntity.getCourse().getDepartment())) {
            return "해당 학과의 학생만 신청할 수 있습니다!";
        }

        Integer majorCount = enrollmentRepository.countMajorCoursesByStudent(student.getUserId());

        // 전공 최대 4개
        if (CourseType.MAJOR.equals(classEntity.getCourse().getType()) && majorCount >= 4) {
            return "한 학기에 최대 4개의 전공 과목만 신청할 수 있습니다!";
        }

        // 0점일경우 Optional
        Integer totalCredits = Optional.ofNullable(enrollmentRepository.getTotalCreditsByStudent(student.getUserId())).orElse(0);
        // 수강 신청 시, 18학점을 초과하는지 확인
        if (totalCredits + classEntity.getCourse().getCredit() > 18) {
            return "최대 수강 가능 학점(18학점)을 초과하였습니다!";
        }

        // 시간표 중복 방지
        if (enrollmentRepository.existsByStudentAndTimeConflict(
                student.getUserId(), classEntity.getDay(), classEntity.getStartTime(), classEntity.getEndTime())) {
            return "이미 같은 시간대에 신청한 강의가 있습니다!";
        }

        // 학년 제한
        int studentYear = studentInfoService.calculateStudentYear(student.getUserId());
        int courseYear = classEntity.getCourse().getYear();

        if (studentYear < courseYear) {
            return "본인 학년보다 높은 학년의 강의는 신청할 수 없습니다!";
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setEnrolledClassEntity(classEntity);
        enrollment.setEnrollmentDate(LocalDate.now());

        enrollmentRepository.save(enrollment);

        // 학생 수 1 증가
        classEntity.setEnrolled(classEntity.getEnrolled() + 1);
        classEntityRepository.save(classEntity);
        return "수강 신청에 성공하였습니다";
    }
}