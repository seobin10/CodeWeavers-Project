package com.green.codeweavers.academymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassesDTO {
    private int classId;
    private String classSemester;
    private String classesSchedule;
    private int courseId;
    private int professorId;
}
