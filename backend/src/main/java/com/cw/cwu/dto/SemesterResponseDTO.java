package com.cw.cwu.dto;

import com.cw.cwu.domain.SemesterTerm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemesterResponseDTO {
    private Integer semesterId;
    private Integer year;
    private SemesterTerm term;
    private LocalDate startDate;
    private LocalDate endDate;
}
