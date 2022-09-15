package com.inflearn.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
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

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
	/*
	@LoadBalanced를 사용하면
	http://127.0.0.1:8000/order-service/%s/orders 이렇게 요청하던 것을
	http://order-service/order-service/%s/orders
	(http://MS name/URI) 형태로 요청할 수 있다.
	 */

}
