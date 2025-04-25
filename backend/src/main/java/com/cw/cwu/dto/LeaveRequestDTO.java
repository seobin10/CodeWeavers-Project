package com.cw.cwu.dto;

import com.cw.cwu.domain.LeaveReason;
import com.cw.cwu.domain.RequestStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LeaveRequestDTO {
    private Integer leaveId;
    private String student;
    private String reason;
    private String reasonDetail;
    private LocalDate requestDate;
    private int expectedSemester;
    private String status;
    private LocalDate approvedDate;
    private String denialReason;

    // ENUM TO STRING (휴학 이유)
    public static String leaveReasonToString(LeaveReason reason) {
        if (reason == null) return null;
        return switch (reason) {
            case MILITARY -> "군대";
            case FAMILY -> "가정사";
            case ILLNESS -> "질병";
            case PERSONAL -> "개인사정";
            case STUDY_ABROAD -> "유학";
            case OTHER -> "기타";
            default -> reason.name();
        };
    }

    // STRING TO ENUM (휴학 이유)
    public LeaveReason leaveReasonToEnum(String str) {
        return switch (str) {
            case "군대" -> LeaveReason.MILITARY;
            case "가정사" -> LeaveReason.FAMILY;
            case "질병" -> LeaveReason.ILLNESS;
            case "개인사정" -> LeaveReason.PERSONAL;
            case "유학" -> LeaveReason.STUDY_ABROAD;
            case "기타" -> LeaveReason.OTHER;
            default -> LeaveReason.valueOf(str);
        };
    }

    // ENUM TO STRING (승인 상태)
    public static String requestStatusToString(RequestStatus status) {
        if (status == null) return null;
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


