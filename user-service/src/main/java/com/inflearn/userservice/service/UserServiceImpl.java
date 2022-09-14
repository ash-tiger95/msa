package com.inflearn.userservice.service;

import com.inflearn.userservice.dto.UserDto;
import com.inflearn.userservice.jpa.UserEntity;
import com.inflearn.userservice.repository.UserRepository;
import com.inflearn.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment env;
    RestTemplate restTemplate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder, Environment env, RestTemplate restTemplate){ // 생성자가 스프링부트 컨텍스트에서 만들어지면서 빈을 등록하고 메모리에 올린다.
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
        this.env = env;
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

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null){
            throw new UsernameNotFoundException("User not found"); // Spring Security에서 사용되는 로그인 인증에 사용되는 exception (적절하지 않지만 그냥 씀)
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        /* Using as rest template */
        String orderUrl = String.format(env.getProperty("order_service.url"), userId); // String.format을 하는 이유는 %s에 userId를 넣기 위함 (http://127.0.0.1:8000/order-service/%s/orders)
        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET,
                null, // 파라미터 (필요없음, 우리가 요청할 때 파라미터 포함해서 보내니깐)
                new ParameterizedTypeReference<List<ResponseOrder>>() { // 어떤 형식으로 받을 건지
        });

        List<ResponseOrder> orderList = orderListResponse.getBody();
        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    /*
        extends UserDetailsService에서 Override 해줘야함
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username); // 우리는 Email
        if(userEntity == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true,true,
                true,true, 
                new ArrayList<>() // 권한을 추가하는 작업을 하는 곳
        ) ; // Spring Security의 User()
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null){
            throw new UsernameNotFoundException(email);
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

}
