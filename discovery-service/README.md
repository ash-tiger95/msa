# Discovery Service

``` yml
server:
  port: 8761

spring:
  application:
    name: discovery-service

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

- Discovery-Service는 Web Server 역할임에 port를 설정해줘야한다.
- spring.application.name은 필수
- register-with-eureka, fetch-registry: client로 사용여부를 설정하는 곳인데, 자기 자신을 client로 사용하지 않는데 등록하는 건 무의미하여 false

```java
package com.inflearn.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServiceApplication.class, args);
	}

}
```
- @EnableEurekaServer: Eureka 서버로 등록

## Eureka Dashboard
> http://localhost:8761/
-  마이크로 서비스를 등록하면 해당 서비스를 확인할 수 있다.