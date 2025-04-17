package com.cw.cwu.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GradeStatusResponseDTO {
    private boolean hasStudentRecords;
    private List<GradeStatusDTO> gradeStatusList;
}
