package com.cw.cwu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Service
public class DepartmentSimpleDTO {
    private Integer departmentId;
    private String departmentName;
}