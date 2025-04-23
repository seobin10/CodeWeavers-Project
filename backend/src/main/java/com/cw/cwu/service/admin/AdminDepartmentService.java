package com.cw.cwu.service.admin;

import com.cw.cwu.dto.DepartmentCreateRequestDTO;
import com.cw.cwu.dto.DepartmentDetailDTO;
import com.cw.cwu.dto.DepartmentUpdateRequestDTO;

public interface AdminDepartmentService {
    DepartmentDetailDTO getDepartmentDetail(Integer id);


    void createDepartment(DepartmentCreateRequestDTO dto);

    void updateDepartment(Integer id, DepartmentUpdateRequestDTO dto);

    void deleteDepartment(Integer id);
}
