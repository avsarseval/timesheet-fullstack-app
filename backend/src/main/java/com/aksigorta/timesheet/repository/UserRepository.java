package com.aksigorta.timesheet.repository;

import com.aksigorta.timesheet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Bu arayüzün bir Spring veritabanı bileşeni olduğunu belirtir (isteğe bağlı ama iyi bir pratik).
public interface UserRepository extends JpaRepository<User, Long> { //extend edince springden temel db metotları gelir
    // <User, Long> şu anlama gelir: "Ben User nesneleri ile çalışacağım
    // ve onların ID'si Long tipindedir."
}