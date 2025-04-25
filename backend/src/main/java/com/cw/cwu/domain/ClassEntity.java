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
@ToString
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "professor_id", referencedColumnName = "user_id", nullable = true)
    private User professor;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "class_day", nullable = false)
    private String day;

    @Column(name = "class_start", nullable = false)
    private Integer startTime;

    @Column(name = "class_end", nullable = false)
    private Integer endTime;

    @Column(name = "class_capacity", nullable = false)
    private Integer capacity;

    @Column(name = "class_enrolled", nullable = false)
    private Integer enrolled;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id", nullable = false)
    private LectureRoom lectureRoom;
}

