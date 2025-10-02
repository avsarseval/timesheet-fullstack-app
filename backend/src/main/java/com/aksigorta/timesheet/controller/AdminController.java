package com.aksigorta.timesheet.controller;

import com.aksigorta.timesheet.dto.TimesheetCreateRequestDto;
import com.aksigorta.timesheet.dto.TimesheetResponseDto;
import com.aksigorta.timesheet.service.AdminService;
import com.aksigorta.timesheet.mapper.TimesheetMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final TimesheetMapper timesheetMapper; // Update sonrası DTO'ya çevirmek için

    public AdminController(AdminService adminService, TimesheetMapper timesheetMapper) {
        this.adminService = adminService;
        this.timesheetMapper = timesheetMapper;
    }

    @GetMapping("/timesheets/all")
    public ResponseEntity<Page<TimesheetResponseDto>> getAllTimesheets(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(adminService.getAllTimesheets(page, size));
    }

    @PutMapping("/timesheets/{id}")
    public ResponseEntity<TimesheetResponseDto> adminUpdateTimesheet(
            @PathVariable Long id,
            @RequestBody TimesheetCreateRequestDto requestDto) {

        TimesheetResponseDto responseDto = adminService.adminUpdateTimesheet(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/timesheets/{id}")
    public ResponseEntity<String> adminDeleteTimesheet(@PathVariable Long id) {
        adminService.adminDeleteTimesheet(id);
        return ResponseEntity.ok("Timesheet (ID: " + id + ") deleted successfully by admin.");
    }
    @GetMapping("/timesheets/export/csv")
    public void exportAllTimesheets(HttpServletResponse response) throws IOException {
        adminService.exportAllTimesheetsToCsv(response);
    }
}