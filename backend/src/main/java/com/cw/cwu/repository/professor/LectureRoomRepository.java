package com.cw.cwu.repository.professor;

import com.cw.cwu.domain.LectureRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LectureRoomRepository extends CrudRepository<LectureRoom, Integer> {

    @Override
    List<LectureRoom> findAll();
}
