package com.cw.cwu.dto;

import com.cw.cwu.domain.SemesterTerm;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemesterDTO {
    private Integer semesterId;
    private Integer year;
    private SemesterTerm term;
}