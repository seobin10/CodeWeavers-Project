package com.cw.cwu.dto;

import com.cw.cwu.domain.SemesterTerm;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SemesterRequestDTO {
    private Integer year;
    private SemesterTerm term;
    private LocalDate startDate;
    private LocalDate endDate;
}
