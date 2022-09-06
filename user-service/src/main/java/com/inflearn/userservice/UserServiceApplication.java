package com.inflearn.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	/*
		UserServiceImpl에서 @Service를 등록하면서 @Autowired로 생성자에 적힌 빈들을 등록하는데 초기화 작업이 필요하다.
		이 초기화 작업은 이전에 빈 값이 미리 준비되어 있어야해서 여기서 초기화시켜준다.
		이럴 경우 타입만 같으면 된다.
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
