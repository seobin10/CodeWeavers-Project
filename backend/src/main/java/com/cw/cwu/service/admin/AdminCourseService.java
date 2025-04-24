package com.cw.cwu.service.admin;


import com.cw.cwu.dto.CourseCreateRequestDTO;
import com.cw.cwu.dto.CourseInfoDTO;
import com.cw.cwu.dto.CourseUpdateRequestDTO;

import java.util.List;

public interface AdminCourseService {

    List<CourseInfoDTO> getCoursesByFilter(String departmentId);

    void createCourse(CourseCreateRequestDTO dto);

    void updateCourse(Integer id, CourseUpdateRequestDTO dto);

    void deleteCourse(Integer id);
}
