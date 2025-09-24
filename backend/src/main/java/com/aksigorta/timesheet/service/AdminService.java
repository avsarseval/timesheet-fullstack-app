package com.aksigorta.timesheet.service;

import com.aksigorta.timesheet.dto.TimesheetCreateRequestDto;
import com.aksigorta.timesheet.dto.TimesheetResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface AdminService {

    Page<TimesheetResponseDto> getAllTimesheets(int page, int size);
    TimesheetResponseDto adminUpdateTimesheet(Long timesheetId, TimesheetCreateRequestDto requestDto);    void adminDeleteTimesheet(Long timesheetId);
    void exportAllTimesheetsToCsv(HttpServletResponse response) throws IOException;


}