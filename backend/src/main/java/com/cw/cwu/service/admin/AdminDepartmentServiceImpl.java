package com.cw.cwu.service.admin;

import com.cw.cwu.domain.Department;
import com.cw.cwu.domain.DepartmentStatus;
import com.cw.cwu.domain.UserRole;
import com.cw.cwu.dto.DepartmentCreateRequestDTO;
import com.cw.cwu.dto.DepartmentDetailDTO;
import com.cw.cwu.dto.DepartmentUpdateRequestDTO;
import com.cw.cwu.repository.ClassEntityRepository;
import com.cw.cwu.repository.CourseRepository;
import com.cw.cwu.repository.DepartmentRepository;
import com.cw.cwu.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminDepartmentServiceImpl implements AdminDepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ClassEntityRepository classEntityRepository;

    @Override
    public DepartmentDetailDTO getDepartmentDetail(Integer id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("학과가 존재하지 않음"));

        int studentCount = userRepository.countByDepartment_DepartmentIdAndUserRole(id, UserRole.STUDENT);
        int professorCount = userRepository.countByDepartment_DepartmentIdAndUserRole(id, UserRole.PROFESSOR);
        int courseCount = courseRepository.countByDepartment_DepartmentId(id);
        int classCount = classEntityRepository.countClassesByDepartmentId(id);

        return new DepartmentDetailDTO(
                dept.getDepartmentId(),
                dept.getDepartmentName(),
                dept.getStatus(),
                studentCount,
                professorCount,
                courseCount,
                classCount
        );
    }


    @Override
    public void createDepartment(DepartmentCreateRequestDTO dto) {
        if (departmentRepository.existsByDepartmentName(dto.getDepartmentName())) {
            throw new IllegalArgumentException("이미 존재하는 학과입니다.");
        }

        Department dept = Department.builder()
                .departmentName(dto.getDepartmentName())
                .status(DepartmentStatus.AVAILABLE)
                .build();

        departmentRepository.save(dept);
    }


    @Override
    public void updateDepartment(Integer id, DepartmentUpdateRequestDTO dto) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("학과가 존재하지 않음"));

        if (dto.getNewName() != null && !dto.getNewName().isBlank()) {
            // 중복 이름 방지
            if (departmentRepository.existsByDepartmentName(dto.getNewName()) &&
                    !dto.getNewName().equals(dept.getDepartmentName())) {
                throw new IllegalArgumentException("이미 존재하는 학과명입니다.");
            }
            dept.setDepartmentName(dto.getNewName());
        }

        if (dto.getNewStatus() != null) {
            dept.setStatus(dto.getNewStatus());
        }

        departmentRepository.save(dept);
    }

    @Override
    public void deleteDepartment(Integer id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("학과가 존재하지 않음");
        }

        departmentRepository.deleteById(id);
    }


}
