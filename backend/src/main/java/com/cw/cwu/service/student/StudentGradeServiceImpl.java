    package com.cw.cwu.service.student;

    import com.cw.cwu.domain.*;
    import com.cw.cwu.dto.GradeDTO;
    import com.cw.cwu.repository.*;
    import com.cw.cwu.service.user.UserSemesterService;
    import lombok.RequiredArgsConstructor;
    import org.modelmapper.ModelMapper;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class StudentGradeServiceImpl implements StudentGradeService {


        private final GradeRepository gradeRepository;
        private final ModelMapper modelMapper;
        private final UserSemesterService userSemesterService;
        private final UserRepository userRepository;
        private final StudentRecordRepository studentRecordRepository;


        private double convertGradeToPoint(StudentGrade grade) {
            if (grade == null) return 0.0;
            return switch (grade) {
                case A_PLUS -> 4.5;
                case A0 -> 4.0;
                case B_PLUS -> 3.5;
                case B0 -> 3.0;
                case C_PLUS -> 2.5;
                case C0 -> 2.0;
                case D_PLUS -> 1.5;
                case D0 -> 1.0;
                case F -> 0.0;
            };
        }

        // 학생 성적 조회 (현재 학기만)
        @Override
        public List<GradeDTO> getStudentGrade(String studentId) {
            // 🔥 현재 학기 ID 가져오기
            Semester currentSemester = userSemesterService.getCurrentSemester();
            Integer semesterId = currentSemester.getId();

            // 🔥 semesterId로 필터링된 성적 조회
            return gradeRepository.findGrade(studentId, semesterId)
                    .stream()
                    .map(grade -> {
                        GradeDTO dto = modelMapper.map(grade, GradeDTO.class);

                        // 성적 변환
                        String changeGrade = dto.ConvertToDb(grade.getGrade());
                        dto.setGrade(changeGrade);

                        // 평점 변환
                        double gradePoint = convertGradeToPoint(grade.getGrade());
                        dto.setGradePoint(gradePoint);

                        // 전공/교양, 과목명, 학점
                        Course course = grade.getEnrollment().getEnrolledClassEntity().getCourse();
                        dto.setCourseType(
                                course.getType() == CourseType.MAJOR ? "전공" : "교양"
                        );
                        dto.setCourseName(course.getName());
                        dto.setCredit(course.getCredit());

                        // 학기 문자열
                        Semester semester = grade.getEnrollment().getEnrolledClassEntity().getSemester();
                        String semesterString = semester.getYear() + "-" +
                                (semester.getTerm().name().equals("FIRST") ? "1" : "2");
                        dto.setSemester(semesterString);

                        return dto;
                    })
                    .collect(Collectors.toList());
        }

        @Override
        public StudentRecord getStudentRecord(String studentId) {
            Semester currentSemester = userSemesterService.getCurrentSemester();
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 학생을 찾을 수 없습니다."));

            return studentRecordRepository.findByStudentAndSemester(student, currentSemester)
                    .orElse(null);
        }


//        // 학생 성적 조회
//        @Override
//        public List<GradeDTO> getStudentGrade(String studentId) {
//            return gradeRepository.findGrade(studentId)
//                    .stream()
//                    .map(grade -> {
//                        GradeDTO dto = modelMapper.map(grade, GradeDTO.class);
//
//                        // 1. 성적 (ENUM → 문자열 변환)
//                        String changeGrade = dto.ConvertToDb(grade.getGrade());
//                        dto.setGrade(changeGrade);
//
//                        // 2. 평점변환 (A+ → 4.5)
//                        double gradePoint = convertGradeToPoint(grade.getGrade());
//                        dto.setGradePoint(gradePoint);
//
//                        // 3. 전공/교양 구분
//                        Course course = grade.getEnrollment().getEnrolledClassEntity().getCourse();
//                        dto.setCourseType(
//                                course.getType() == CourseType.MAJOR ? "전공" : "교양"
//                        );
//
//                        dto.setCourseName(course.getName());
//                        dto.setCredit(course.getCredit());
//
//                        // 4. 학기 (2024-1 형태)
//                        Semester semester = grade.getEnrollment().getEnrolledClassEntity().getSemester();
//                        String semesterString = semester.getYear() + "-" +
//                                (semester.getTerm().name().equals("FIRST") ? "1" : "2");
//                        dto.setSemester(semesterString);
//
//                        return dto;
//                    })
//                    .collect(Collectors.toList());
//
//        }

    }