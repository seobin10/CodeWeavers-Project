package com.cw.cwu.service.admin;

import com.cw.cwu.domain.Department;
import com.cw.cwu.domain.DepartmentStatus;
import com.cw.cwu.domain.ScheduleType;
import com.cw.cwu.domain.UserRole;
import com.cw.cwu.dto.DepartmentCreateRequestDTO;
import com.cw.cwu.dto.DepartmentDetailDTO;
import com.cw.cwu.dto.DepartmentUpdateRequestDTO;
import com.cw.cwu.repository.ClassEntityRepository;
import com.cw.cwu.repository.CourseRepository;
import com.cw.cwu.repository.DepartmentRepository;
import com.cw.cwu.repository.UserRepository;
import com.cw.cwu.service.user.UserSemesterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminDepartmentServiceImpl implements AdminDepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ClassEntityRepository classEntityRepository;
    private final UserSemesterService userSemesterService;
    private final AdminScheduleService adminScheduleService;

    public List<Integer> resolveRelevantSemesterIds() {
        List<Integer> result = new ArrayList<>();
        try {
            result.add(userSemesterService.getCurrentSemester().getId());
        } catch (Exception ignored) {}

        try {
            Integer openSemester = adminScheduleService.getSemesterIdByScheduleType(ScheduleType.CLASS);
            if (!result.contains(openSemester)) result.add(openSemester);
        } catch (Exception ignored) {}

        adminScheduleService.getUpcomingSemesterId()
                .ifPresent(id -> {
                    if (!result.contains(id)) result.add(id);
                });

        return result;
    }

    @Override
    public List<DepartmentDetailDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return departments.parallelStream().map(dept -> {
            Integer id = dept.getDepartmentId();

            int studentCount = userRepository.countByDepartment_DepartmentIdAndUserRole(id, UserRole.STUDENT);
            int professorCount = userRepository.countByDepartment_DepartmentIdAndUserRole(id, UserRole.PROFESSOR);
            int courseCount = courseRepository.countByDepartment_DepartmentId(id);

            List<Integer> relevantSemesterIds = resolveRelevantSemesterIds();
            int classCount = classEntityRepository.countByCourse_Department_DepartmentIdAndSemester_IdIn(id, relevantSemesterIds);

            return new DepartmentDetailDTO(
                    dept.getDepartmentId(),
                    dept.getDepartmentName(),
                    dept.getStatus(),
                    studentCount,
                    professorCount,
                    courseCount,
                    classCount
            );
    }).toList();
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

        // 상태를 UNAVAILABLE로 변경할 경우 제약 검사
        if (dto.getNewStatus() == DepartmentStatus.UNAVAILABLE) {
            List<Integer> relevantSemesterIds = resolveRelevantSemesterIds();

            boolean hasClass = classEntityRepository.existsByCourse_Department_DepartmentIdAndSemester_IdIn(id, relevantSemesterIds);

            if (hasClass) {
                throw new IllegalStateException("해당 학과는 현재 강의가 등록되어 있어 운영을 중지할 수 없습니다.");
            }
        }

        if (dto.getNewName() != null && !dto.getNewName().isBlank()) {
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
            throw new EntityNotFoundException("해당 학과가 존재하지 않습니다.");
        }

        try {
            departmentRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("이 학과는 사용 이력이 있어 삭제할 수 없습니다.");
        }
    }


}
