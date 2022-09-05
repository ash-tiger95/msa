package com.inflearn.userservice.service;

import com.inflearn.userservice.dto.UserDto;
import com.inflearn.userservice.jpa.UserEntity;
import com.inflearn.userservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // mapper 설정, 전략으로 딱 맞아 떨어져야한다.

        UserEntity userEntity = mapper.map(userDto, UserEntity.class); // userDto를 UserEntity로 변환한다.
        userEntity.setEncryptedPwd("encrypted_password");

        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }
}
