# User-Service

```yml
server:
  port: 9001

spring:
  application:
    name: user-service

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url: http://127.0.0.1:8761/eureka
```
- register-with-eureka, fetch-registry: Eureka Client로 사용하겠다.
- service-url: Eureka Server URL (http://127.0.0.1:8761/eureka)에 마이크로 서비스를 등록하겠다.

```java
package com.inflearn.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}

```

- @EnableDiscoveryClient: Eureka Client로 사용하겠다.


# Service 여러 개 실행하는 방법

방법1) 실행버튼
방법2) 