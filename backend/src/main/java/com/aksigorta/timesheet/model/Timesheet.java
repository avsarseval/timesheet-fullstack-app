package com.aksigorta.timesheet.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "timesheets") //çoğul olmamalı
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String description;


    // Birçok timesheet girişi tek bir kullanıcıya ait olabilir
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // timesheets tablosunda user_id adında bir sütun oluşturur.
    private User user;
}