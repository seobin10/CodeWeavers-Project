package com.cw.cwu.service.student;

import com.cw.cwu.domain.ReturnRequest;
import com.cw.cwu.domain.Semester;
import com.cw.cwu.domain.User;
import com.cw.cwu.dto.ReturnRequestDTO;
import com.cw.cwu.repository.ReturnRequestRepository;
import com.cw.cwu.repository.SemesterRepository;
import com.cw.cwu.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentReturnRequestServiceImpl implements StudentReturnRequestService{

    private final UserRepository userRepository;
    private final ReturnRequestRepository returnRepository;
    private final SemesterRepository semesterRepository;
    private final ModelMapper modelMapper;

    // 복학 신청 메서드
    @Override
    @Transactional
    public Integer requestReturn(ReturnRequestDTO dto, String studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("해당하는 학생 데이터를 찾을 수 없습니다."));

        Semester semester = semesterRepository.findById(dto.getSemester())
                .orElseThrow(() -> new RuntimeException("해당하는 학기 데이터를 찾을 수 없습니다."));

        ReturnRequest request = ReturnRequest.builder()
                .returnId(dto.getReturnId())
                .student(student)
                .requestDate(dto.getRequestDate())
                .semester(semester)
                .status(dto.requestStatusToEnum(dto.getStatus()))
                .approvedDate(dto.getApprovedDate())
                .denialReason(dto.getDenialReason())
                .build();
        ReturnRequest result = returnRepository.save(request);
        return result.getReturnId();
    }

    // Semester, studentId 타입 처리
    @PostConstruct
    public void initMapper() {
        var typeMap = modelMapper.createTypeMap(ReturnRequest.class, ReturnRequestDTO.class);
        typeMap.addMappings(m -> m.map(
                src -> src.getStudent().getUserId(),
                ReturnRequestDTO::setStudent
        ));
        typeMap.addMappings(m -> m.map(
                src -> src.getSemester().getId(),
                ReturnRequestDTO::setSemester
        ));
        typeMap.addMappings(m -> m.skip(ReturnRequestDTO::setStatus));
        typeMap.setPostConverter(ctx -> {
            ReturnRequest source = ctx.getSource();
            ReturnRequestDTO dest = ctx.getDestination();
            dest.setStatus(ReturnRequestDTO.requestStatusToString(source.getStatus()));
            return dest;
        });
    }

    // 복학 내역 조회
    @Override
    public List<ReturnRequestDTO> findReturnData(String studentId) {
        List<ReturnRequest> returnRequests = returnRepository.findAllByStudent_UserId(studentId);

        List<ReturnRequestDTO> dto = returnRequests.stream()
                .map(data -> modelMapper.map(data, ReturnRequestDTO.class))
                .collect(Collectors.toList());
        return dto;
    }
}
