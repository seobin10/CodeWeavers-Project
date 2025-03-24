package com.cw.cwu.service.student;

import com.cw.cwu.domain.ClassEntity;
import com.cw.cwu.domain.CourseType;
import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.EnrollmentRequestDTO;
import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import com.cw.cwu.repository.student.ClassRepository;
import com.cw.cwu.repository.student.EnrollmentRepository;
import com.cw.cwu.repository.student.FilterRepository;
import com.cw.cwu.repository.student.StudentRepository;
import com.cw.cwu.repository.user.UserRepository;
import com.cw.cwu.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

    private final StudentRepository studentRepository;
    private final FilterRepository filterRepository;
    private final ModelMapper modelMapper;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final StudentInfoService studentInfoService;

    // 학생이 수강 신청 가능한 강의 목록 조회
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

        Page<Map<String, Object>> result = studentRepository.findAvailableCoursesPaged(
                studentId, courseType, departmentName, courseYear,
                classDay, classStart, credit, courseName, pageable
        );

        return PageUtil.toDTO(result, pageRequestDTO.getPage());
    }

    // 학과 목록 필터링
    public List<Map<String, Object>> getDepartments() {
        return filterRepository.findDistinctDepartments().stream()
                .map(d -> Map.<String, Object>of("departmentName", d))
                .collect(Collectors.toList());
    }

    // 강의 구분 필터링
    public List<Map<String, Object>> getCourseTypes() {
        return filterRepository.findDistinctCourseTypes().stream()
                .map(ct -> Map.<String, Object>of("courseType",
                        ct.equals("MAJOR") ? "전공" : "교양"))
                .collect(Collectors.toList());
    }

    // 강의 학년 필터링
    public List<Map<String, Object>> getCourseYears() {
        return filterRepository.findDistinctCourseYears().stream()
                .map(y -> Map.<String, Object>of("courseYear", y))
                .collect(Collectors.toList());
    }

    // 강의 요일 필터링
    public List<Map<String, Object>> getClassDays() {
        return filterRepository.findDistinctClassDays().stream()
                .map(cd -> Map.<String, Object>of("classDay", cd))
                .collect(Collectors.toList());
    }

    // 강의 시작 시간 필터링
    public List<Map<String, Object>> getClassTimes() {
        return filterRepository.findDistinctClassTimes().stream()
                .map(ct -> Map.<String, Object>of("classTime", ct))
                .collect(Collectors.toList());
    }

    // 학점 목록 조회 필터링
    public List<Map<String, Object>> getCredits() {
        return filterRepository.findDistinctCredits().stream()
                .map(c -> Map.<String, Object>of("credit", c))
                .collect(Collectors.toList());
    }

    // 수강 신청 처리
    public String applyToClass(EnrollmentRequestDTO requestDTO) {
        System.out.println("service applyToClass : " + requestDTO);
        User student = userRepository.getReferenceById(requestDTO.getStudentId());

        ClassEntity classEntity = classRepository.getReferenceById(requestDTO.getClassId());

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
        classRepository.save(classEntity);
        return "수강 신청에 성공하였습니다";
    }
}