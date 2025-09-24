package com.aksigorta.timesheet.mapper;

import com.aksigorta.timesheet.dto.UserRegisterRequestDto;
import com.aksigorta.timesheet.dto.UserResponseDto;
import com.aksigorta.timesheet.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toUserResponseDto(User user);

    User toUserEntity(UserRegisterRequestDto requestDto);
}