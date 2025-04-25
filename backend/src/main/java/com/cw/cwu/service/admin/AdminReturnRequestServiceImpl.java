package com.cw.cwu.service.admin;

import com.cw.cwu.domain.ReturnRequest;
import com.cw.cwu.dto.ReturnRequestDTO;
import com.cw.cwu.repository.ReturnRequestRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReturnRequestServiceImpl implements AdminReturnRequestService {

    private final ReturnRequestRepository returnRepository;
    private final ModelMapper modelMapper;

    // 복학 처리 메서드
    @Override
    @Transactional
    public void responseReturn(ReturnRequestDTO dto) {
        Optional<ReturnRequest> result = returnRepository.findById(dto.getReturnId());
        ReturnRequest request = result.orElseThrow();
        String studentId = request.getStudent().getUserId();

        request.changeStatus(dto.getStatus());
        request.changeDenialReason(dto.getDenialReason());
        request.changeApprovedDate();
        returnRepository.save(request);
    }

    // 복학 내역 전체 조회 메서드
    @Override
    public List<ReturnRequestDTO> findAllReturnData() {
        List<ReturnRequest> returnRequests = returnRepository.findAll();

        List<ReturnRequestDTO> dto = returnRequests.stream()
                .map(data -> modelMapper.map(data, ReturnRequestDTO.class))
                .collect(Collectors.toList());
        return dto;
    }

    @Override
    public String findReturnUserName(Integer returnId) {
        ReturnRequest request = returnRepository.findById(returnId)
                .orElseThrow(() -> new RuntimeException("복학 신청 없음"));
        return request.getStudent().getUserName();
    }
}
