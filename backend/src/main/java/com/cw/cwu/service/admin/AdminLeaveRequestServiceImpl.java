package com.cw.cwu.service.admin;

import com.cw.cwu.domain.*;
import com.cw.cwu.dto.LeaveRequestDTO;
import com.cw.cwu.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminLeaveRequestServiceImpl implements AdminLeaveRequestService {

    private final LeaveRequestRepository leaveRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SemesterRepository semesterRepository;
    private final UserRepository userRepository;
    private final StudentRecordRepository studentRecordRepository;
    private final ClassEntityRepository classRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void responseLeave(LeaveRequestDTO dto) {
        Optional<LeaveRequest> result = leaveRepository.findById(dto.getLeaveId());
        LeaveRequest request = result.orElseThrow();
        String studentId = request.getStudent().getUserId();

        request.changeStatus(dto.getStatus());
        request.changeDenialReason(dto.getDenialReason());
        request.changeApprovedDate();

        if (Objects.equals(dto.getStatus(), "APPROVED")) {
            // 수강 삭제(학기 구분없이 전역 삭제되므로, 수정이 필요할 수 있음)
            List<Enrollment> enrollments = enrollmentRepository.findByStudent_UserId(studentId);

            for (Enrollment enrollment : enrollments) {
                ClassEntity classEntity = enrollment.getEnrolledClassEntity();
                if (classEntity != null) {
                    int currentEnrolled = Optional.ofNullable(classEntity.getEnrolled()).orElse(0);
                    classEntity.setEnrolled(Math.max(0, currentEnrolled - 1));
                    classRepository.save(classEntity);
                }
                enrollmentRepository.delete(enrollment);
            }

            // 현재 학기 조회하여 레코드 삭제
            LocalDate now = LocalDate.now();
            Optional<Semester> optionalCurrentSemester = semesterRepository.findCurrentSemester(now);
            if (optionalCurrentSemester.isPresent()) {
                Semester currentSemester = optionalCurrentSemester.get();
                User student = userRepository.findById(studentId).orElseThrow();
                Optional<StudentRecord> optionalRecord = studentRecordRepository.findByStudentAndSemester(student, currentSemester);
                optionalRecord.ifPresent(studentRecordRepository::delete);
            }
        }
        leaveRepository.save(request);
    }


    // 휴학 전체 조회
    @Override
    public List<LeaveRequestDTO> findAllLeaveData() {
        List<LeaveRequest> leaveRequests = leaveRepository.findAll();

        List<LeaveRequestDTO> dto = leaveRequests.stream()
                .map(data -> modelMapper.map(data, LeaveRequestDTO.class))
                .collect(Collectors.toList());
        return dto;
    }

    @Override
    public String findLeaveUserName(Integer leaveId) {
        LeaveRequest request = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("휴학 신청 없음"));
        return request.getStudent().getUserName();
    }

}
