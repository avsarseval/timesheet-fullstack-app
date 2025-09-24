package com.aksigorta.timesheet.service.impl;

import com.aksigorta.timesheet.dto.UserRegisterRequestDto;
import com.aksigorta.timesheet.dto.UserResponseDto;
import com.aksigorta.timesheet.mapper.UserMapper; // Mapper import'u
import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.repository.UserRepository;
import com.aksigorta.timesheet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper; // Mapper'ı sınıfımıza dahil ediyoruz.

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper) { // Constructor'a mapper'ı ekliyoruz.
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto registerUser(UserRegisterRequestDto requestDto) {
        // Gelen Request DTO'sunu, veritabanına kaydedilecek User Entity'sine dönüştür.
        User user = userMapper.toUserEntity(requestDto);

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);

        // Veritabanına kaydedilen Entity'yi, Controller'a güvenli bir şekilde döndürmek için Response DTO'suna dönüştür.
        return userMapper.toUserResponseDto(savedUser);
    }
}