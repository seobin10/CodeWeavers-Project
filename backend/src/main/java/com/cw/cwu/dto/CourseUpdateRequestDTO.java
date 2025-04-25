package com.cw.cwu.dto;

import com.cw.cwu.domain.CourseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseUpdateRequestDTO {
    private String newName;
    private Integer newCredit;
    private Integer newCourseYear;
    private CourseStatus newStatus;
}

