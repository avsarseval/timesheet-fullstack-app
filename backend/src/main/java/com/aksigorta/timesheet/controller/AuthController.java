package com.aksigorta.timesheet.controller;

import com.aksigorta.timesheet.dto.UserRegisterRequestDto;
import com.aksigorta.timesheet.dto.UserResponseDto;
import com.aksigorta.timesheet.service.UserService;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")

    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegisterRequestDto requestDto) {
        UserResponseDto responseDto = userService.registerUser(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
        String jwt = jwtTokenProvider.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        System.out.println(">>> BACKEND'DE ÜRETİLEN TOKEN: " + jwt);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}