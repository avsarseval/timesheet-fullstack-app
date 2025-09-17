package com.aksigorta.timesheet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Bu sınıfın bir Spring konfigürasyon sınıfı olduğunu belirtir.
@EnableWebSecurity // Spring Security'nin web güvenliğini aktive etmesini sağlar.
public class SecuirtyConfig {

    // @Bean anotasyonu, bu metodun ürettiği nesnenin Spring tarafından yönetileceğini belirtir.
    // Bu, projemizin ana güvenlik kuralı zinciridir.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF koruması, stateful (session kullanan) uygulamalar için önemlidir.
                // Biz stateless (JWT kullanan) bir REST API yaptığımız için şimdilik devre dışı bırakıyoruz.
                .csrf(AbstractHttpConfigurer::disable)

                // Gelen HTTP istekleri için yetkilendirme kurallarını burada tanımlıyoruz.
                .authorizeHttpRequests(auth -> auth
                        // "/api/auth/" altındaki TÜM adreslere (örn: /register, /login)
                        // gelen isteklere HİÇBİR KİMLİK KONTROLÜ YAPMADAN izin ver.
                        .requestMatchers("/api/auth/**").permitAll()

                        // Yukardaki kuralın DIŞINDA KALAN diğer TÜM istekler için
                        // ise mutlaka kimlik doğrulaması (authentication) iste.
                        .anyRequest().authenticated()
                );

        // Oluşturulan güvenlik yapılandırmasını döndür.
        return http.build();
    }
}