package com.aksigorta.timesheet.service;

import com.aksigorta.timesheet.dto.TimesheetDto;
import com.aksigorta.timesheet.model.Timesheet;
import com.aksigorta.timesheet.model.User;
import org.springframework.data.domain.Page;

public interface TimesheetService {

    Timesheet createTimesheet(Timesheet timesheet, User user);
    Page<TimesheetDto> getTimesheetsForUser (User user, int page, int size);
    Timesheet updateTimesheet(Long timesheetId, Timesheet updatedTimesheet, User user);
    void deleteTimesheet(Long timesheetId, User user);

    Page<TimesheetDto> getAllTimesheets(int page, int size);
    Timesheet adminUpdateTimesheet(Long timesheetId, Timesheet updatedTimesheet);
    void adminDeleteTimesheet(Long timesheetId);

    TimesheetDto convertToDto(Timesheet timesheet);
}