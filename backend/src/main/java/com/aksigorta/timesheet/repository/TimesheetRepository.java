package com.aksigorta.timesheet.repository;

import com.aksigorta.timesheet.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List; // List import'unu eklemeyi unutma

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    // Spring Data JPA'nın sihri burada: Metodun ismini doğru yazarsan,
    // senin için "SELECT * FROM timesheets WHERE user_id = ?" sorgusunu
    // otomatik olarak oluşturur.
    List<Timesheet> findAllByUserId(Long userId);
}