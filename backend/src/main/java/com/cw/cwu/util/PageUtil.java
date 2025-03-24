package com.cw.cwu.util;

import com.cw.cwu.dto.PageResponseDTO;
import org.springframework.data.domain.Page;

public class PageUtil {

    public static <T> PageResponseDTO<T> toDTO(Page<T> page, int currentPage) {
        return PageResponseDTO.<T>builder()
                .dtoList(page.getContent())
                .totalPage(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .current(currentPage)
                .build();
    }
}
