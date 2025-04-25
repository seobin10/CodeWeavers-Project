package com.cw.cwu.repository;

import com.cw.cwu.domain.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Integer> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);

}
