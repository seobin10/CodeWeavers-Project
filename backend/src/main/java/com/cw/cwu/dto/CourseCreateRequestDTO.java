package com.cw.cwu.dto;

import com.cw.cwu.domain.CourseStatus;
import com.cw.cwu.domain.CourseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCreateRequestDTO {
    private String courseName;
    private CourseType courseType;
    private Integer credit;
    private Integer courseYear;
    private Integer departmentId;
    private CourseStatus status;
}

