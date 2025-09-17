package com.aksigorta.timesheet.model;

import jakarta.persistence.*;
import lombok.Data; // Lombok'tan gelen @Data anotasyonu

@Data // Getter, Setter, toString, EqualsAndHashCode metotlarını otomatik oluşturur.
@Entity // Bu sınıfın bir veritabanı tablosuna karşılık geldiğini belirtir.
@Table(name = "users") // Tablonun adını "users" olarak belirler.
public class User {

    @Id // Bu alanın birincil anahtar (primary key) olduğunu belirtir.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID'nin veritabanı tarafından otomatik artırılacağını belirtir.
    private Long id;

    @Column(unique = true, nullable = false) // Bu sütunun benzersiz ve boş olamaz olduğunu belirtir.
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role; // Örneğin: "ROLE_USER" veya "ROLE_ADMIN"
}