package com.cw.cwu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassDTO {
    private Integer classId;
    private String courseName;
    private String semester;
    private String day;
    private Integer startTime;
    private Integer endTime;
    private Integer capacity;
    private Integer enrolled;
    private String lectureRoomName;
    private String buildingName;

    private String courseType;
    private Integer courseYear;
    private Boolean isCurrentSemester;
}