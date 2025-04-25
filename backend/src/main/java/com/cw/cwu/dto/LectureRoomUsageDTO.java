package com.cw.cwu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureRoomUsageDTO {

    private Integer classId;
    private String courseName;
    private String professorName;
    private String professorPhone;
    private String departmentName;
    private String day;
    private Integer startTime;
    private Integer endTime;
    private String lectureRoomName;
    private String buildingName;
}