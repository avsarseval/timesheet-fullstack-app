package com.aksigorta.timesheet.service;

import com.aksigorta.timesheet.dto.UserRegisterRequestDto;
import com.aksigorta.timesheet.dto.UserResponseDto;

public interface UserService {


    UserResponseDto registerUser(UserRegisterRequestDto requestDto);
}