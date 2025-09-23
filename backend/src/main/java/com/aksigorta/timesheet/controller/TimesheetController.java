package com.aksigorta.timesheet.controller;

import com.aksigorta.timesheet.model.Timesheet;
import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.security.CustomUserDetails;
import com.aksigorta.timesheet.service.TimesheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.aksigorta.timesheet.dto.TimesheetDto;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {

    private final TimesheetService timesheetService;

    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }


    private User getCurrentUser(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getUser();
    }

    @PostMapping
    public ResponseEntity<String> createTimesheet(@RequestBody Timesheet timesheet, Authentication authentication) {
        timesheetService.createTimesheet(timesheet, getCurrentUser(authentication));
        return ResponseEntity.ok("Timesheet created successfully!");
    }

    @GetMapping
    public ResponseEntity<Page<TimesheetDto>> getMyTimesheets(
            Authentication authentication,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        // @RequestParam, URL'deki sorgu parametrelerini (örn: ?page=1&size=5) okur.
        // defaultValue, eğer parametre gönderilmezse varsayılan değerleri belirler.

        Page<TimesheetDto> timesheetDtosPage = timesheetService.getTimesheetsForUser(getCurrentUser(authentication), page, size);
        return ResponseEntity.ok(timesheetDtosPage);
    }

   @PutMapping("/{id}")
    public ResponseEntity<TimesheetDto> updateTimesheet(@PathVariable Long id, @RequestBody Timesheet timesheetDetails, Authentication authentication) {
        // 1. Servisten güncellenmiş entity'yi al.
        Timesheet updatedTimesheet = timesheetService.updateTimesheet(id, timesheetDetails, getCurrentUser(authentication));

        // 2. Cevabı dönmeden önce DTO'ya çevir.
        TimesheetDto timesheetDto = timesheetService.convertToDto(updatedTimesheet);

        return ResponseEntity.ok(timesheetDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<TimesheetDto>> getAllTimesheets(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<TimesheetDto> timesheetDtosPage = timesheetService.getAllTimesheets(page, size);
        return ResponseEntity.ok(timesheetDtosPage);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimesheetDto> adminUpdateTimesheet(@PathVariable Long id, @RequestBody Timesheet timesheetDetails) {
        Timesheet updatedTimesheet = timesheetService.adminUpdateTimesheet(id, timesheetDetails);
        return ResponseEntity.ok(timesheetService.convertToDto(updatedTimesheet));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDeleteTimesheet(@PathVariable Long id) {
        timesheetService.adminDeleteTimesheet(id);
        return ResponseEntity.ok("Timesheet (ID: " + id + ") deleted successfully by admin.");
    }
}