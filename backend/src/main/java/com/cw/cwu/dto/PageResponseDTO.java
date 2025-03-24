package com.cw.cwu.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponseDTO<T> {
    private List<T> dtoList;
    private int totalPage;
    private int current;
    private long totalCount;
}
