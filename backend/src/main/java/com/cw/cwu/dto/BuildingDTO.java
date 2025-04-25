package com.cw.cwu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildingDTO {
    private Integer buildingId;
    private String buildingName;
    private String status;
}