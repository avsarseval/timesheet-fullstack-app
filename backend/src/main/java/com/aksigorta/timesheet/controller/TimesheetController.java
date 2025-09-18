package com.aksigorta.timesheet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timesheets") // Bu adres "/api/auth/" ile başlamadığı için korumalı olacak!
public class TimesheetController {

    @GetMapping("/test")
    public ResponseEntity<String> getProtectedRoute() {
        return ResponseEntity.ok("Tebrikler! Korunan bir alana başarıyla eriştiniz. JWT'niz geçerli.");
    }
}