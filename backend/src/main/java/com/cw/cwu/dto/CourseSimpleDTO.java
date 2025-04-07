package com.cw.cwu.dto;

import com.cw.cwu.domain.CourseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseSimpleDTO {
    private Integer courseId;
    private String courseName;
    private CourseType courseType;
    private Integer courseYear;
}