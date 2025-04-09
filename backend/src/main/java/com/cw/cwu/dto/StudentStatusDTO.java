package com.cw.cwu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentStatusDTO {
    private String userId;
    private int studentYear;
    private boolean graduationEligible;
}
