package com.cw.cwu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureRoomStatusDTO {
    private Integer roomId;
    private String roomName;
    private String status;
    private String buildingName;
}