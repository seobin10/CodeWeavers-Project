package com.green.codeweavers.academymanager.dto;

import com.green.codeweavers.academymanager.domain.Classes;
import com.green.codeweavers.academymanager.domain.Departments;
import com.green.codeweavers.academymanager.domain.UserRole;
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
public class CoursesDTO {
    private int courseId;
    private String courseName;
    private List<String> courseType;
    private int credit;
    private int departmentId;
}
