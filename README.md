# MSA

Study MSA used SpringBoot

# 프로젝트 구성

|       서비스       |                역할                |                      설명                      |
| :----------------: | :--------------------------------: | :--------------------------------------------: |
| discovery-service  | Spring Cloud Netflix Eureka Server |                                                |
|    user-service    | Spring Cloud Netflix Eureka Client |                                                |
|    zuul-service    |        Netflix-Zuul (동기)         |              API Gateway, Filter               |
|   first-service    |            Netflix Zuul            |                                                |
|   second-service   |            Netflix Zuul            |                                                |
| apigateway-service |   Spring Cloud Gateway (비동기)    | zuul-service와 같은 역할 (API Gateway, Filter) |

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

# Spring Cloud Gateway

역할: Zuul 대신 사용 (비동기 o)

apigateway-service를 실행하면 Netty라는 비동기 내장서버가 작동한다. (이전에는 톰캣)

Zuul과 다른점

|                   요청URL                   |     first-service Controller      | zuul-service | apigateway-service |
| :-----------------------------------------: | :-------------------------------: | :----------: | :----------------: |
| http://localhost:8081/first-service/welcome |       @RequestMapping("/")        |      o       |         x          |
| http://localhost:8081/first-service/welcome | @RequestMapping("/first-service") |      x       |         o          |

# Spring Cloud Gateway - Filter

client <-> Spring Cloud Gateway <-> MicroService

방법1) Java

방법2) Property(.yml)

종류) Filter,

- Custom Filter(

  - Pre/Post Filter
  - 설정할 라우트마다 지정해야 한다.

- Global Filter

  - 라우트 정보가 달라도 공통적으로 실행되는 필터
  - 한번만 설정해주면 된다.
  - 필터 중 가장 처음 실행되고 가장 마지막에 실행된다.

# Configuration 정보(.yml args) 불러오는 방법

예제) .yml

> greeting: message: Welcome yml

방법1) Environment 사용

- env.getProperty("greeting.message")

방법2) @Value 사용

- @Value("${greeting.message}")

---

---

---

# PJT Service 정리

|      구성요소      | 설명                                           |
| :----------------: | :--------------------------------------------- |
|   Git Repository   | 마이크로서비스 소스 관리 및 프로파일 관리      |
|   Config Server    | Git 저장소에 등록된 프로파일 정보 및 설정 정보 |
|   Eureka Server    | 마이크로서비스 등록 및 검색                    |
| API Gateway Server | 마이크로서비스 부하 분산 및 서비스 라우팅      |
|   Microservices    | 회원 MS, 주문 MS, 상품(카데고리)MS             |
|   Queuing System   | 마이크로서비스 간 메시지 발행 및 구독          |

# 애플리케이션 APIs

| 마이크로서비스  | Restful API                                                | HTTP Method |
| :-------------: | :--------------------------------------------------------- | :---------: |
| Catalog Service | /catalog-service/catalogs: 상품 목록 제공                  |     GET     |
|  User Service   | /user-service/users: 사용자 정보 등록                      |    POST     |
|  User Service   | /user-service/users: 전체 사용자 조회                      |     GET     |
|  User Service   | /user-service/users/{user_id}: 사용자 정보, 주문 내역 조회 |     GET     |
|  Order Service  | /order-service/users/{user_id}/orders: 주문 등록           |    POST     |
|  Order Service  | /order-service/users/{user_id}/orders: 주문 조회           |     GET     |

# User-Service

| 기능                        | URI(API Gateway 사용 시)         | URI(API Gateway 미사용 시) | HTTP Method |
| :-------------------------- | :------------------------------- | :------------------------- | :---------: |
| 사용자 정보 등록            | /user-service                    | /users                     |    POST     |
| 전체 사용자 조회            | /user-service/users              | /users                     |     GET     |
| 사용자 정보, 주문 내역 조회 | /user-service/users/{user_id}    | /users/{user_id}           |     GET     |
| 작동 상태 확인              | /user-service/users/health_check | /health_check              |     GET     |
| 환영 메시지                 | /user-service/users/welcome      | /welcome                   |     GET     |
| 로그인                      | /user-service/login              | /login                     |    POST     |

# Cataologs-Service

| 기능                     | URI(API Gateway 사용 시) | URI(API Gateway 미사용 시)      | HTTP Method |
| :----------------------- | :----------------------- | :------------------------------ | :---------: |
| 상품 목록 조회           | Catalogs Microservice    | /catalog-service/catalogs       |     GET     |
| 사용자 별 상품 주문      | Order Microservice       | /order-service/{user_id}/orders |    POST     |
| 사용자 별 주문 내역 조회 | Orders Microservice      | /order-service/{user_id}/orders |     GET     |

## Spring Security

역할: Authentication(인증) + Authorization(권한)

### 작동순서

1. API Gateway Routes 수정
2. @Configuration이 먼저 스프링부트 빈에 등록된다.
   > 우리서비스에서는 WebSecurity 클래스가 먼저 빈에 등록됨
3. 로그인을 시도했을 때 제일 먼저 실행되는 AutenticationFilter의 attemptAuthentication 함수
4. AutenticationFilter의 successfulAuthentication

# Spring Cloud Config (Configuration Service)

application.yml(구성정보) 내용이 바뀌면 새로 빌드해야되는 번거로움 해소

- 분산 시스템에서 서버, 클라이언트 구성에 필요한 설정 정보(application.yml)를 외부 시스템에서 관리

- 하나의 중앙화 된 저장소에서 구성요소 관리 기능

- 각 서비스를 다시 빌드하지 않고, 바로 적용 가능

- 애플리케이션 배포 파이프라인을 통해 DEV-UAT-PROD 환경에 맞는 구성 정보 사용

---

---

---

## keytool

Private Key 만들기

> keytool -genkeypair -alias apiEncryptionKey -keyalg RSA -dname "CN=Ahn, OU=API Development, o=test.co.kr, L=Seoul, C=KR" -keypass "test1234" -keystore apiEncryptionKey.jks -storepass "test1234"

Public Key 추출하기

> keytool -export -alias apiEncryptionKey -keystore apiEncryptionKey.jks -rfc -file trustServer.cer

> keytool -import -alias trustServer -file trustServer.cer -keystore publicKey.jks

## Tip

jpd: hibernate: ddl-auto: create-drop

> 초기데이터 저장 (SQL파일 이용해 바로 INSERT)
