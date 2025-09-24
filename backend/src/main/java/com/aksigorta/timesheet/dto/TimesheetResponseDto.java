package com.aksigorta.timesheet.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimesheetResponseDto(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String description,
        String username // user nesnesinin tamamı yerine sadece username
) {
}