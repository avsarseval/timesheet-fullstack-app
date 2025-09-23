package com.aksigorta.timesheet.service.impl;

import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.repository.UserRepository;
import com.aksigorta.timesheet.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.aksigorta.timesheet.dto.UserDto;

@Service 
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Şifreleri hash'lemek için.

    // Spring'in UserRepository ve PasswordEncoder' otomatik olarak bulup bu sınıfa vermesini sağlar.
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public User registerUser(User user) {

        // Gelen kullanıcının şifresini asla olduğu gibi kaydetmek yerine hashle
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Default rol ata
        user.setRole("ROLE_USER");

        // Hazırlanan kullanıcıyı dbye kaydolması icin Repositoriye ver.
        return userRepository.save(user);
    }
}