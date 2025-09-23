package com.aksigorta.timesheet.repository;

import com.aksigorta.timesheet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { //extend edince springden temel db metotları gelir
    Optional<User> findByUsername(String username);
}

