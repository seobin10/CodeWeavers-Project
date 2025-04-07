package com.cw.cwu.dto;

import com.cw.cwu.domain.ScheduleType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDTO {
    private Integer semesterId;
    private ScheduleType scheduleType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

}