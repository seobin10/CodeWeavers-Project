package com.cw.cwu.repository;

import com.cw.cwu.domain.LectureRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LectureRoomRepository extends CrudRepository<LectureRoom, Integer> {

    @Override
    List<LectureRoom> findAll();

    List<LectureRoom> findByBuilding_Id(Integer buildingId);

    boolean existsByBuilding_IdAndName(Integer buildingId, String name);

    boolean existsByBuilding_IdAndNameAndIdNot(Integer buildingId, String name, Integer id);
}
