package com.green.codeweavers.academymanager.dto;

import com.green.codeweavers.academymanager.domain.Classes;
import com.green.codeweavers.academymanager.domain.Grades;
import com.green.codeweavers.academymanager.domain.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EnrollmentDTO {
    private int enrollmentId;
    private Date enrollmentDate;
    private int studentId;
    private int classId;
}
