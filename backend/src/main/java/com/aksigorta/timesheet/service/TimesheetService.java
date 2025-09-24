package com.aksigorta.timesheet.service;

import com.aksigorta.timesheet.dto.TimesheetCreateRequestDto;
import com.aksigorta.timesheet.dto.TimesheetResponseDto;
import com.aksigorta.timesheet.model.User;
import org.springframework.data.domain.Page;

public interface TimesheetService {

    TimesheetResponseDto createTimesheet(TimesheetCreateRequestDto requestDto, User user);

    Page<TimesheetResponseDto> getTimesheetsForUser(User user, int page, int size);

    TimesheetResponseDto updateTimesheet(Long timesheetId, TimesheetCreateRequestDto requestDto, User user);

    void deleteTimesheet(Long timesheetId, User user);
}