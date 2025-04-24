package com.cw.cwu.dto;

import com.cw.cwu.domain.CourseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfoDTO {
    private Integer courseId;
    private String courseName;
    private CourseType courseType;
    private Integer credit;
    private Integer courseYear;
    private String status;

}
