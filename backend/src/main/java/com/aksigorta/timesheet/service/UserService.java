package com.aksigorta.timesheet.service;

import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Bu önemli!
import org.springframework.stereotype.Service;

@Service // Bu sınıfın iş mantığı katmanı olduğunu Spring'e söyler.
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Şifreleri hash'lemek için.

    // Dependency Injection: Spring'in bizim için UserRepository ve PasswordEncoder'ı
    // otomatik olarak bulup bu sınıfa vermesini sağlar.
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        // Tarife başlıyoruz:
        // 1. Gelen kullanıcının şifresini asla olduğu gibi kaydetme.
        //    Bunun yerine, geri döndürülemez bir şekilde hash'le.
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // 2. Kullanıcıya varsayılan bir rol ata.
        user.setRole("ROLE_USER");

        // 3. Hazırlanan kullanıcıyı veritabanına kaydetmesi için Repository'ye (Kiler Görevlisine) ver.
        return userRepository.save(user);
    }
}