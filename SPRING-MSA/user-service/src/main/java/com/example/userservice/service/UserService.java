package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();    // 데이터베이스에서 읽은 데이터를 있는 그대로 출력
    UserDto getUserDetailByEmail(String userName);

    UserDto updateByUserId(String userId, UserDto userDto);
}
