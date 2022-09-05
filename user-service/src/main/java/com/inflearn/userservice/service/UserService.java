package com.inflearn.userservice.service;


import com.inflearn.userservice.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
}
