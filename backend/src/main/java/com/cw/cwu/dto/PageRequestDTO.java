package com.cw.cwu.dto;

import lombok.Data;

@Data
public class PageRequestDTO {
    private int page = 1;
    private int size = 15;

    private String keyword;   // 이름 or ID 검색
    private String sortField; // 정렬 기준 컬럼명 (예: userName)
    private String sortDir;   // 정렬 방향 (asc, desc)
}
