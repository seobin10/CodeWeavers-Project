package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "leave_requests",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "expected_semester_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Integer leaveId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private LeaveReason reason;

    @Column(name = "reason_detail", nullable = false)
    private String reasonDetail;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @ManyToOne
    @JoinColumn(name = "expected_semester_id", nullable = false)
    private Semester expectedSemester;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "approved_date")
    private LocalDate approvedDate;

    @Column(name = "denial_reason")
    private String denialReason;
}
