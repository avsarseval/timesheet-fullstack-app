package com.aksigorta.timesheet.dto;

// Sadece dış dünyanın görmesini istediğimiz alanları içerir.
public record UserDto(
        Long id,
        String username,
        String email,
        String role
) {
}