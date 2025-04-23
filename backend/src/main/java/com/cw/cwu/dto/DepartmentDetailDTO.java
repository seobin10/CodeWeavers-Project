package com.cw.cwu.dto;

import com.cw.cwu.domain.DepartmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDetailDTO {
    private Integer departmentId;
    private String departmentName;
    private DepartmentStatus status;

    private int studentCount;
    private int professorCount;
    private int courseCount;
    private int classCount;
}
