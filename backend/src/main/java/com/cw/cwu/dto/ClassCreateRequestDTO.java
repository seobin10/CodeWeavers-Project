package com.cw.cwu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassCreateRequestDTO {
    private Integer courseId;
    private String professorId;
    private String day;
    private Integer startTime;
    private Integer endTime;
    private Integer capacity;
    private Integer lectureRoomId;
}
