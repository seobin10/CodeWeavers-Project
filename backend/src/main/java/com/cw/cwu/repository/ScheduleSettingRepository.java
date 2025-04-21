package com.cw.cwu.repository;

import com.cw.cwu.domain.ScheduleSetting;
import com.cw.cwu.domain.ScheduleType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.Optional;


public interface ScheduleSettingRepository extends JpaRepository<ScheduleSetting, Integer> {

    Optional<ScheduleSetting> findBySemesterIdAndScheduleType(Integer semesterId, ScheduleType type);

    boolean existsBySemesterId(Integer semesterId);

    Optional<ScheduleSetting> findByScheduleTypeAndStartDateBeforeAndEndDateAfter(
            ScheduleType scheduleType,
            LocalDateTime start,
            LocalDateTime end
    );
}
