package com.aksigorta.timesheet.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimesheetCreateRequestDto(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String description
) {
}