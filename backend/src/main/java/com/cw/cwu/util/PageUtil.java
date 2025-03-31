package com.cw.cwu.util;

import com.cw.cwu.dto.PageRequestDTO;
import com.cw.cwu.dto.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtil {

    public static <T> PageResponseDTO<T> toDTO(Page<T> page, int currentPage) {
        return PageResponseDTO.<T>builder()
                .dtoList(page.getContent())
                .totalPage(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .current(currentPage)
                .build();
    }

    public static Pageable toPageable(PageRequestDTO dto, String defaultSortField) {
        String sortField = dto.getSortField() != null ? dto.getSortField() : defaultSortField;
        String sortDir = dto.getSortDir() != null ? dto.getSortDir() : "asc";

        // 커스텀 정렬 필드 매핑
        switch (sortField) {
            case "departmentName":
                sortField = "department.departmentName";
                break;
            case "buildingName":
                sortField = "building.buildingName";
                break;
        }

        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortField
        );

        return PageRequest.of(dto.getPage() - 1, dto.getSize(), sort);
    }
}
