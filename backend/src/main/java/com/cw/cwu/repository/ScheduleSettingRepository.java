package com.cw.cwu.repository;

import com.cw.cwu.domain.ScheduleSetting;
import com.cw.cwu.domain.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ScheduleSettingRepository extends JpaRepository<ScheduleSetting, Integer> {

    Optional<ScheduleSetting> findBySemesterIdAndScheduleType(Integer semesterId, ScheduleType type);

    boolean existsBySemesterId(Integer semesterId);
}
