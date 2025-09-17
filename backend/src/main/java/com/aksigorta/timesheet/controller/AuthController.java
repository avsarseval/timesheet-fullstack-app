package com.aksigorta.timesheet.controller;

import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aksigorta.timesheet.dto.AuthResponse;
import com.aksigorta.timesheet.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.aksigorta.timesheet.security.JwtTokenProvider;

@RestController // Bu sınıfın bir REST API controller'ı olduğunu ve JSON döneceğini belirtir.
@RequestMapping("/api/auth") // Bu controller'daki tüm adreslerin "/api/auth" ile başlayacağını söyler.
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    // "/api/auth" ile "/register" birleşir ve tam adres "/api/auth/register" olur.
    // @PostMapping: Bu metodun sadece POST isteklerini dinleyeceğini söyler.
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // @RequestBody: Gelen JSON verisini bir User nesnesine dönüştürür.
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser); // Başarılı olursa, kaydedilen kullanıcıyı geri döner.
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kimlik doğrulama başarılı, şimdi JWT basma zamanı!
        String token = jwtTokenProvider.generateToken(authentication);

        // Cevap olarak token'ı içeren AuthResponse nesnesini dön.
        return ResponseEntity.ok(new AuthResponse(token));
    }
}