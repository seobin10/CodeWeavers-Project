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
public class DepartmentUpdateRequestDTO {
    private String newName;
    private DepartmentStatus newStatus;
}
