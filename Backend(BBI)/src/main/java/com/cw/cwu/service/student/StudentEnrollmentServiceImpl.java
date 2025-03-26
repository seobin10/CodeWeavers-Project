package com.cw.cwu.service.student;

import com.cw.cwu.domain.ClassEntity;
import com.cw.cwu.domain.CourseType;
import com.cw.cwu.domain.Enrollment;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.EnrollmentRequestDTO;
import com.cw.cwu.repository.ClassRepository;
import com.cw.cwu.repository.EnrollmentRepository;
import com.cw.cwu.repository.student.FilterRepository;
import com.cw.cwu.repository.student.StudentRepository;
import com.cw.cwu.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
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

    @Override
    public List<Map<String, Object>> getAvailableCourses(String studentId, String courseType, String departmentName, Integer courseYear, String classDay, Integer classStart, Integer credit, String courseName) {
        return studentRepository.findAvailableCourses(studentId, courseType, departmentName, courseYear, classDay, classStart, credit, courseName);
    }

    @Override
    public List<Map<String, Object>> getDepartments() {
        return filterRepository.findDistinctDepartments().stream()
                .map(d -> Map.<String, Object>of("departmentName", d))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getCourseTypes() {
        return filterRepository.findDistinctCourseTypes().stream()
                .map(ct -> Map.<String, Object>of("courseType", ct.equals("MAJOR") ? "전공" : "교양"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getCourseYears() {
        return filterRepository.findDistinctCourseYears().stream()
                .map(y -> Map.<String, Object>of("courseYear", y))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getClassDays() {
        return filterRepository.findDistinctClassDays().stream()
                .map(cd -> Map.<String, Object>of("classDay", cd))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getClassTimes() {
        return filterRepository.findDistinctClassTimes().stream()
                .map(ct -> Map.<String, Object>of("classTime", ct))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getCredits() {
        return filterRepository.findDistinctCredits().stream()
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
                    courseMap.put("담당교수", c.getProfessor().getName());
                    return courseMap;
                }).collect(Collectors.toList());
    }

    @Override
    public String deleteCourse(String studentId, Integer classId) {
        User student = userRepository.findByUserId(studentId).orElseThrow();
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow();
        Optional<Enrollment> optional = enrollmentRepository.findByStudentAndEnrolledClassEntity(student, classEntity);
        if (optional.isPresent()) {
            enrollmentRepository.delete(optional.get());
            System.out.println("강의 삭제 완료: studentId=" + studentId + ", classId=" + classId);
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
                .map(e -> {
                    ClassEntity c = e.getEnrolledClassEntity();
                    Map<String, Object> courseMap = new HashMap<>();
                    courseMap.put("강의번호", c.getId());
                    courseMap.put("courseName", c.getCourse().getName());
                    courseMap.put("classDay", c.getDay());
                    courseMap.put("classRoom", c.getRoom() != null ? c.getRoom().getRoomName() : "미정");
                    courseMap.put("professorName", c.getProfessor() != null ? c.getProfessor().getName() : "미정");
                    courseMap.put("classStartPeriod", c.getStartTime());
                    courseMap.put("classEndPeriod", c.getEndTime());
                    courseMap.put("classCredit", c.getCourse().getCredit());
                    return courseMap;
                }).collect(Collectors.toList());
    }


    // 수강신청처리
    @Override
    public String applyToClass(EnrollmentRequestDTO requestDTO) {
        User student = userRepository.getReferenceById(requestDTO.getStudentId());
        ClassEntity classEntity = classRepository.getReferenceById(requestDTO.getClassId());

        // 중복신청방지
        if (enrollmentRepository.existsByStudentAndEnrolledClassEntity(student, classEntity)) {
            return "이미 신청한 강의입니다.";
        }

        // 정원 체크
        if (classEntity.getEnrolled() >= classEntity.getCapacity()) {
            return "수강 정원이 초과되었습니다!";
        }

        //학과제한
        if (classEntity.getCourse().getDepartment() != null &&
                !student.getDepartment().equals(classEntity.getCourse().getDepartment())) {
            return "해당 학과의 학생만 신청할 수 있습니다!";
        }

        // 전공 최대 4개
        Integer majorCount = enrollmentRepository.countMajorCoursesByStudent(student.getUserId());
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
