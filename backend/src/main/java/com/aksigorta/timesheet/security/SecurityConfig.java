package com.aksigorta.timesheet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Bu import önemli olacak

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // CustomUserDetailsService, Spring'e veritabanından kullanıcıları nasıl bulacağını söyler.
    // Biz bunu zaten yazmıştık. Spring bunu otomatik olarak bulacak.
    private final CustomUserDetailsService customUserDetailsService;

    // JwtAuthenticationFilter, gelen isteklerdeki JWT'yi kontrol edecek olan filtremiz.
    // Henüz yazmadık ama yerini şimdiden hazırlıyoruz. Bu bir sonraki günün konusu.
    // private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor Injection
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
        // this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    // Bu Bean, kimlik doğrulama yöneticisini (AuthenticationManager) sağlar.
    // Bu olmadan /login işlemi çalışmaz.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bu, projemizin ana güvenlik kuralı zinciridir.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF korumasını devre dışı bırakıyoruz çünkü biz JWT kullanıyoruz (stateless).
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Oturum yönetimini STATELESS olarak ayarlıyoruz.
                // Bu, sunucunun asla session oluşturmayacağını ve her isteğin bağımsız olduğunu belirtir.
                // JWT tabanlı uygulamalar için bu ZORUNLUDUR. 403 hatasının ana kaynağı genellikle budur.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. Gelen HTTP istekleri için yetkilendirme kurallarını burada tanımlıyoruz.
                .authorizeHttpRequests(auth -> auth
                        // "/api/auth/" altındaki TÜM adreslere (örn: /register, /login)
                        // gelen isteklere HİÇBİR KİMLİK KONTROLÜ YAPMADAN izin ver (permitAll).
                        .requestMatchers("/api/auth/**").permitAll()

                        // Yukardaki kuralın DIŞINDA KALAN diğer TÜM istekler için
                        // ise mutlaka kimlik doğrulaması (authenticated) iste.
                        .anyRequest().authenticated()
                );

        // 4. (Bu satır 6. gün için ama şimdiden ekleyebiliriz)
        // Kendi yazdığımız JWT filtresini, Spring'in standart kullanıcı adı/şifre filtresinden
        // ÖNCE çalışacak şekilde zincire ekleyeceğiz. Şimdilik yoruma alalım.
        // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        // Oluşturulan güvenlik yapılandırmasını döndür.
        return http.build();
    }
}