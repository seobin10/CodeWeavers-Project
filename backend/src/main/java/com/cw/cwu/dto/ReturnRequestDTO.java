package com.cw.cwu.dto;

import com.cw.cwu.domain.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ReturnRequestDTO {
    private Integer returnId;
    private String student;
    private LocalDate requestDate;
    private Integer semester;
    private String status;
    private LocalDate approvedDate;
    private String denialReason;

    // ENUM TO STRING (승인 상태)
    public static String requestStatusToString(RequestStatus status) {
        return switch (status) {
            case PENDING -> "대기";
            case APPROVED -> "승인";
            case DENIED -> "거절";
            default -> status.name();
        };
    }

    // STRING TO ENUM (승인 상태)
    public RequestStatus requestStatusToEnum(String str) {
        return switch (str) {
            case "대기" -> RequestStatus.PENDING;
            case "승인" -> RequestStatus.APPROVED;
            case "거절" -> RequestStatus.DENIED;
            default -> RequestStatus.valueOf(str);
        };
    }
}
