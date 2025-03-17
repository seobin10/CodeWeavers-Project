package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer classId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course courseId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User professorId;

    @Column(name = "class_semester")
    private String classSemester;

    @Column(name = "class_schedule")
    private String classSchedule;
}
