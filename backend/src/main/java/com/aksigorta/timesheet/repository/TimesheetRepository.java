package com.aksigorta.timesheet.repository;

import com.aksigorta.timesheet.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    Page<Timesheet> findAllByUserId(Long userId, Pageable pageable);
    //named query
    List<Timesheet> findByUserIdAndDateAndStartTimeBeforeAndEndTimeAfter(
            Long userId,
            LocalDate date,
            LocalTime newEndTime,
            LocalTime newStartTime
    );
}