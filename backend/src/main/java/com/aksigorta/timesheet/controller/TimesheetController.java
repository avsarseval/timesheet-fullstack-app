package com.aksigorta.timesheet.controller;

import com.aksigorta.timesheet.model.Timesheet;
import com.aksigorta.timesheet.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Bu import çok önemli!
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {

    private final TimesheetService timesheetService;

    @Autowired
    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    // YENİ BİR ZAMAN ÇİZELGESİ OLUŞTURMAK İÇİN ENDPOINT
    @PostMapping
    public ResponseEntity<Timesheet> createTimesheet(@RequestBody Timesheet timesheet, Authentication authentication) {
        // Spring, "bilekliği" (Authentication nesnesini) otomatik olarak bu metoda parametre olarak verir.
        String username = authentication.getName();
        Timesheet createdTimesheet = timesheetService.createTimesheet(timesheet, username);
        return ResponseEntity.ok(createdTimesheet);
    }

    // GİRİŞ YAPMIŞ KULLANICININ ZAMAN ÇİZELGELERİNİ LİSTELEMEK İÇİN ENDPOINT
    @GetMapping
    public ResponseEntity<List<Timesheet>> getMyTimesheets(Authentication authentication) {
        String username = authentication.getName();
        List<Timesheet> timesheets = timesheetService.getTimesheetsByUsername(username);
        return ResponseEntity.ok(timesheets);
    }
}