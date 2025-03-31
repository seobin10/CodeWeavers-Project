package com.cw.cwu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LectureRoomSimpleDTO {
    private Integer roomId;
    private String roomName;
    private String buildingName;
}