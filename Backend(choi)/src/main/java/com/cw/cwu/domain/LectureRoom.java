package com.cw.cwu.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecture_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer id;

    @Column(name = "room_name", nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;
}
