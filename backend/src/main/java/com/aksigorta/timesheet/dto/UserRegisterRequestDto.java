package com.aksigorta.timesheet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Bu bir "kayıt isteği"dir. Sadece gerekli bilgileri içerir.
public record UserRegisterRequestDto(
        @NotBlank(message = "Username cannot be blank")
        String username,

        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
        String password
) {
}