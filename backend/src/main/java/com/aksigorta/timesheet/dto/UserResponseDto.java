package com.aksigorta.timesheet.dto;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        String role
) {
}