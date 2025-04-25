package com.cw.cwu.service.student;

import com.cw.cwu.domain.LeaveRequest;
import com.cw.cwu.domain.Semester;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.LeaveRequestDTO;
import com.cw.cwu.repository.LeaveRequestRepository;
import com.cw.cwu.repository.SemesterRepository;
import com.cw.cwu.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentLeaveRequestServiceImpl implements StudentLeaveRequestService {

    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRepository;
    private final SemesterRepository semesterRepository;
    private final ModelMapper modelMapper;

    // 휴학 신청 처리 메서드
    @Override
    public Integer requestLeave(LeaveRequestDTO dto, String studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("해당하는 학생 데이터를 찾을 수 없습니다."));

        Semester semester = semesterRepository.findById(dto.getExpectedSemester())
                .orElseThrow(() -> new RuntimeException("해당하는 학기 데이터를 찾을 수 없습니다."));

        LeaveRequest request = LeaveRequest.builder()
                .leaveId(dto.getLeaveId())
                .student(student)
                .reason(dto.leaveReasonToEnum(dto.getReason()))
                .requestDate(dto.getRequestDate())
                .expectedSemester(semester)
                .status(dto.requestStatusToEnum(dto.getStatus()))
                .approvedDate(dto.getApprovedDate())
                .denialReason(dto.getDenialReason())
                .reasonDetail(dto.getReasonDetail())
                .build();
        LeaveRequest result = leaveRepository.save(request);
        return result.getLeaveId();
    }

    // Semester, studentId 타입 처리
    @PostConstruct
    public void initMapper() {
        var typeMap = modelMapper.createTypeMap(LeaveRequest.class, LeaveRequestDTO.class);
        typeMap.addMappings(m -> m.map(
                src -> src.getStudent().getUserId(),
                LeaveRequestDTO::setStudent
        ));
        typeMap.addMappings(m -> m.map(
                src -> src.getExpectedSemester().getId(),
                LeaveRequestDTO::setExpectedSemester
        ));
        typeMap.addMappings(m -> m.skip(LeaveRequestDTO::setReason));
        typeMap.addMappings(m -> m.skip(LeaveRequestDTO::setStatus));
        typeMap.setPostConverter(ctx -> {
            LeaveRequest source = ctx.getSource();
            LeaveRequestDTO dest = ctx.getDestination();
            dest.setReason(LeaveRequestDTO.leaveReasonToString(source.getReason()));
            dest.setStatus(LeaveRequestDTO.requestStatusToString(source.getStatus()));
            return dest;
        });
    }

    // 자신의 휴학 데이터를 찾는 메서드
    @Override
    public List<LeaveRequestDTO> findLeaveData(String studentId) {
        List<LeaveRequest> leaveRequests = leaveRepository.findAllByStudent_UserId(studentId);

        List<LeaveRequestDTO> dto = leaveRequests.stream()
                .map(data -> modelMapper.map(data, LeaveRequestDTO.class))
                .collect(Collectors.toList());
        return dto;
    }
}
