# MSA

Study MSA used SpringBoot

# 프로젝트 에러

1 error
role: org.apache.maven.model.validation.ModelValidator
roleHint: ide

> File > Settings > Build, Execution, Deployment > Build Tools > Maven > Maven home path 설정

# 프로젝트 실행방법

> -> 강의) Service Discovery - User Service 동록

방법1) run 버튼

방법2) Edit Configuration

> VM options에 -Dserver.port=9002 추가

방법3) 인텔리제이 터미널

> mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=9003'

방법4) 빌드

> 1. mvn clean (빌드파일 삭제)
> 2. mvn compile package (타겟 폴더, 스냅샷 생성)
> 3. java -jar -Dserver.port=9004 ./target/user-service-0.0.1-SNAPSHOT.jar

<strong>방법5) 랜덤포트</strong>

매번 실행될 때마다 랜덤한 포트로 실행된다. (인스턴스 충돌이 안된다는 장점)

> server.port: 0

> mvn spring-boot:run으로 포트 설정없이 실행할 수 있다.

eureka.instance.instance-id: ${spring.cloud.client.hostname}:${spring.application.instance_id:${random.value}}

위의 모든 작업으로 Eureka Server 인스턴스가 생성된다.

# Spring Cloud Netflix Eureka

역할: 요청정보에 따른 마이크로서비스 위치를 알려준다.

dependency: Eureks Server

@SpringBootApplication: main함수 역할, 스프링부트는 가지고 있는 모든 클래스 중 이 어노테이션을 검색하고 실행
@EnableEurekaServer: service discovery 서버 등록

# MS
