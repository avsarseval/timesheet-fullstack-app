package com.aksigorta.timesheet.security;

import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Boş yetki listesi için

@Service // Bu sınıfın bir Spring servisi olduğunu belirtir.
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring Security, bir kullanıcıyı doğrulamak için BU metodu çağırır.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Bizim UserRepository'mizi kullanarak veritabanından kullanıcıyı bul.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 2. Bizim User nesnemizi, Spring Security'nin anladığı UserDetails nesnesine dönüştür.
        // Şimdilik rolleri/yetkileri boş bir liste olarak veriyoruz.
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}