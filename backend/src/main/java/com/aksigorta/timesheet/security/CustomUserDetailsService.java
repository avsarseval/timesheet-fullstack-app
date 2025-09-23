package com.aksigorta.timesheet.security;

import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Veritabanından kullanıcıyı, bizim UserRepository'mizi kullanarak bul.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 2. Bulunan User entity'sini, hem Spring Security'nin standartlarını karşılayan
        //    hem de bizim User nesnemizin tamamını içinde taşıyan özel CustomUserDetails
        //    kapsülümüze koyarak geri döndür.
        return new CustomUserDetails(user);
    }
}