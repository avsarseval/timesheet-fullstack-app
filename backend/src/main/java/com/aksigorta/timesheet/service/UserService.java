package com.aksigorta.timesheet.service;

import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.dto.UserDto;

public interface UserService {

    User registerUser(User user);
    UserDto convertToDto(User user);

}
