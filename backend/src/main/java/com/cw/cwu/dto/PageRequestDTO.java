package com.cw.cwu.dto;

import lombok.Data;

@Data
public class PageRequestDTO {
    private int page = 1;
    private int size = 15;
}
