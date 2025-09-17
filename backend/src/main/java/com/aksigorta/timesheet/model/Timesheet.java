package com.aksigorta.timesheet.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "timesheets")
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date; // Tarih (YYYY-MM-DD)

    private LocalTime startTime; // Başlangıç saati (HH:MM:SS)

    private LocalTime endTime; // Bitiş saati (HH:MM:SS)

    private String description;

    // Bu anotasyon, Timesheet ve User arasındaki ilişkiyi kurar.
    // Birçok timesheet girişi tek bir kullanıcıya ait olabilir.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // timesheets tablosunda user_id adında bir sütun oluşturur.
    private User user;
}