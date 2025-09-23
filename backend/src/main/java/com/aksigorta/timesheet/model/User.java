package com.aksigorta.timesheet.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users") //Çoğul olmamlı
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID db tarafından otomatik
    private Long id;

    @Column(unique = true, nullable = false) // Bu sütunun benzersiz ve boş olamaz olduğunu belirtir.
    private String username; //name camelcase

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role; //liste olarak tutulabilir
}