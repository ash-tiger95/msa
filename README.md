# MSA

Study MSA used SpringBoot

# 프로젝트 구성

|      서비스       |     역할      |    설명     |
| :---------------: | :-----------: | :---------: |
| discovery-service | Eureka Server |             |
|   user-service    | Eureka Client |             |
|   zuul-service    | Netflix-Zuul  | API Gateway |
|   first-service   | Netflix Zuul  |             |
|  second-service   | Netflix Zuul  |             |

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

# API Gateway Service

역할: 사용자가 설정한 라우팅 설정에 따라서 각각의 엔드포인트로 클라이언트 대신에서 요청해서 전달해주는 프록시 역할

프론터에서 요청하는 방법이 변경되면 서버를 수정해야되는데 다른 곳에서 사용하는 서비스 요청도 변경해야되는데 이를 조율해주는 역할

- 인증 및 권한 부여
- 서비스 검색 통합
- 응답 캐싱
- 정책, 회로 차단기 및 QoS 다시 시도
- 속도 제한
- 부하 분산
- 로깅, 추적, 상관관계
- 헤더, 쿼리 문자열 및 청구 변환
- IP 허용 목록에 추가

# Netflix Ribbon/Zuul

역할: Spring Cloud에서의 MSA간 통신

1. RestTemplate
2. Feign Client (인터페이스)

3. Ribbon: Client side Load Balancer (비동기x, 최근에 잘 안 쓰임, Spring Cloud Ribbon은 Maintenance(새로운 기능 추가 x) 상태)

   - 서비스 이름으로 호출
   - Health Check
   - 장점: IP/Port로 호출하는 것이 아닌, 마이크로서비스 이름으로 호출

4. Netflix Zuul (최근에 잘 안 쓰임, Spring Cloud Ribbon은 Maintenance(새로운 기능 추가 x) 상태)

   - api gateway 역할
