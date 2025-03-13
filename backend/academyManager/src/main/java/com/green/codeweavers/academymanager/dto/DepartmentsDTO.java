package com.green.codeweavers.academymanager.dto;

import com.green.codeweavers.academymanager.domain.Courses;
import com.green.codeweavers.academymanager.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentsDTO {
    private int departmentId;
    private String departmentName;
}

