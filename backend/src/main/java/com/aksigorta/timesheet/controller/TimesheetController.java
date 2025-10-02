package com.aksigorta.timesheet.controller;

import com.aksigorta.timesheet.dto.TimesheetCreateRequestDto;
import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.security.CustomUserDetails;
import com.aksigorta.timesheet.service.TimesheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.aksigorta.timesheet.dto.TimesheetResponseDto;
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
    public ResponseEntity<TimesheetResponseDto> createTimesheet(@RequestBody TimesheetCreateRequestDto requestDto, Authentication authentication) {
        TimesheetResponseDto responseDto = timesheetService.createTimesheet(requestDto, getCurrentUser(authentication));
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<TimesheetResponseDto>> getMyTimesheets(
            Authentication authentication,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        // @RequestParam, URL'deki sorgu parametrelerini (örn: ?page=1&size=5) okur.
        // defaultValue, eğer parametre gönderilmezse varsayılan değerleri belirler.

        Page<TimesheetResponseDto> timesheetDtosPage = timesheetService.getTimesheetsForUser(getCurrentUser(authentication), page, size);
        return ResponseEntity.ok(timesheetDtosPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimesheetResponseDto> updateTimesheet(
            @PathVariable Long id,
            @RequestBody TimesheetCreateRequestDto requestDto, // Artık DTO alıyor
            Authentication authentication) {

        TimesheetResponseDto responseDto = timesheetService.updateTimesheet(id, requestDto, getCurrentUser(authentication));
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTimesheet(@PathVariable Long id, Authentication authentication) {
        timesheetService.deleteTimesheet(id, getCurrentUser(authentication));
        return ResponseEntity.ok("Timesheet deleted successfully!");
    }

}