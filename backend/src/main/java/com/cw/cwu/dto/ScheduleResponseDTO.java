package com.cw.cwu.dto;

import com.cw.cwu.domain.ScheduleType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDTO {
    private Integer semesterId;
    private ScheduleType scheduleType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;

}