package com.inflearn.userservice.service;


import com.inflearn.userservice.dto.UserDto;
import com.inflearn.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    
    // 타입은 편한걸로 (아래 두개 일부로 다르게 함)
    UserDto getUserByUserId(String userId); // 변환해서 사용하던가
    Iterable<UserEntity> getUserByAll(); // 바로 사용하던가

    UserDto getUserDetailsByEmail(String username);
}
