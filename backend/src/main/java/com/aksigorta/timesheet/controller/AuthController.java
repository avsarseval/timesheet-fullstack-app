package com.aksigorta.timesheet.controller;

import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aksigorta.timesheet.dto.AuthResponse;
import com.aksigorta.timesheet.dto.LoginRequest;
import com.aksigorta.timesheet.dto.UserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.aksigorta.timesheet.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth") // Bu controller'daki tüm adreslerin "/api/auth" prefixiyle başlayacağını söyler.
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    // tam adres "/api/auth/register" olur.
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        UserDto userDto = userService.convertToDto(registeredUser);
        return ResponseEntity.ok(userDto); // Başarılı olursa, kaydedilen kullanıcıyı geri döner.
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

        // Kimlik doğrulama başarılıysa jwt
        String token = jwtTokenProvider.generateToken(authentication);

        // Cevap olarak token içeren AuthResponse nesnesini dön.
        return ResponseEntity.ok(new AuthResponse(token));
    }
}