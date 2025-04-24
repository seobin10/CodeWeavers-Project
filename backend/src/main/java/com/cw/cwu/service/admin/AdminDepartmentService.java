package com.cw.cwu.service.admin;

import com.cw.cwu.dto.DepartmentCreateRequestDTO;
import com.cw.cwu.dto.DepartmentDetailDTO;
import com.cw.cwu.dto.DepartmentUpdateRequestDTO;

import java.util.List;

public interface AdminDepartmentService {
    List<DepartmentDetailDTO> getAllDepartments();

    void createDepartment(DepartmentCreateRequestDTO dto);

    void updateDepartment(Integer id, DepartmentUpdateRequestDTO dto);

    void deleteDepartment(Integer id);
}
