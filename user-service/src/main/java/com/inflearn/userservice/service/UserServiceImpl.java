package com.inflearn.userservice.service;

import com.inflearn.userservice.dto.UserDto;
import com.inflearn.userservice.jpa.UserEntity;
import com.inflearn.userservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder){ // 생성자가 스프링부트 컨텍스트에서 만들어지면서 빈을 등록하고 메모리에 올린다.
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // mapper 설정, 전략으로 딱 맞아 떨어져야한다.

        UserEntity userEntity = mapper.map(userDto, UserEntity.class); // userDto를 UserEntity로 변환한다.
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd())); // 패스워드 암호화

        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }
}
