    package com.cw.cwu.service.student;

    import com.cw.cwu.domain.*;
    import com.cw.cwu.dto.GradeDTO;
    import com.cw.cwu.dto.TotalRecordDTO;
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
            Semester currentSemester = userSemesterService.getCurrentSemester();
            Integer semesterId = currentSemester.getId();

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

                        // 전공/교양, 과목명, 학점 + 과목 ID 추가
                        Course course = grade.getEnrollment().getEnrolledClassEntity().getCourse();
                        dto.setCourseId(course.getId());  // 🔥 여기 추가
                        dto.setCourseType(course.getType() == CourseType.MAJOR ? "전공" : "교양");
                        dto.setCourseName(course.getName());
                        dto.setCredit(course.getCredit());

                        // 학기 문자열
                        Semester semester = grade.getEnrollment().getEnrolledClassEntity().getSemester();
                        String semesterString = semester.getYear() + "-" +
                                (semester.getTerm().name().equals("FIRST") ? "1" : "2");
                        dto.setSemester(semesterString);

                        return dto;
                    })
                    .sorted((a, b) -> {
                        int courseTypeCompare = b.getCourseType().compareTo(a.getCourseType());
                        if (courseTypeCompare != 0) {
                            return courseTypeCompare;
                        }
                        return Integer.compare(a.getCourseId(), b.getCourseId());
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

        // 선택 학기 성적 조회
        @Override
        public List<GradeDTO> getStudentGradeBySemester(String studentId, Integer semesterId) {
            return gradeRepository.findGradesByStudentAndSemester(studentId, semesterId)
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
                        dto.setCourseId(course.getId());
                        dto.setCourseType(course.getType() == CourseType.MAJOR ? "전공" : "교양");
                        dto.setCourseName(course.getName());
                        dto.setCredit(course.getCredit());

                        // 학기 문자열
                        Semester semester = grade.getEnrollment().getEnrolledClassEntity().getSemester();
                        String semesterString = semester.getYear() + "-" +
                                (semester.getTerm().name().equals("FIRST") ? "1" : "2");
                        dto.setSemester(semesterString);

                        return dto;
                    })
                    .sorted((a, b) -> {
                        int courseTypeCompare = b.getCourseType().compareTo(a.getCourseType());
                        if (courseTypeCompare != 0) {
                            return courseTypeCompare;
                        }
                        return Integer.compare(a.getCourseId(), b.getCourseId());
                    })
                    .collect(Collectors.toList());
        }


        @Override
        public List<StudentRecord> getAllStudentRecords(String studentId) {
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 학생을 찾을 수 없습니다."));

            List<StudentRecord> records = studentRecordRepository.findByStudent(student);

            // 🔥 정렬 추가
            records.sort((a, b) -> {
                int yearCompare = Integer.compare(a.getSemester().getYear(), b.getSemester().getYear());
                if (yearCompare != 0) {
                    return yearCompare; // 연도 오름차순
                } else {
                    return (a.getSemester().getTerm() == SemesterTerm.FIRST ? 1 : 2)
                            - (b.getSemester().getTerm() == SemesterTerm.FIRST ? 1 : 2);
                }
            });

            return records;
        }

        @Override
        public TotalRecordDTO getTotalRecord(String studentId) {
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 학생을 찾을 수 없습니다."));

            List<StudentRecord> records = studentRecordRepository.findByStudent(student);

            int totalEarned = 0;
            double totalWeightedGpa = 0.0;

            for (StudentRecord record : records) {
                if (record.getEarned() > 0 && record.getGpa() >= 0) {
                    totalEarned += record.getEarned();
                    totalWeightedGpa += record.getEarned() * record.getGpa();
                }
            }

            double totalGpa = totalEarned > 0 ? totalWeightedGpa / totalEarned : 0.0;

            return new TotalRecordDTO(totalEarned, Math.round(totalGpa * 100.0) / 100.0);
        }
    }