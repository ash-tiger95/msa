# MSA

Study MSA used SpringBoot

- java version: 11
- SpringBoot version: 2.7.x

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

<br/>
<br/>

# Apache Kafka

- Apache Software Foundation의 Scaler 언어로 된 오픈 소스 메시지 브로커 프로젝트
  - 메시지 브로커: 리소스에서 메시지를 전달하는 서버
  - 메시지: Text, Json, xml, Java에서는 object 등
- 링크드인(Liked-in)에서 개발, 2011년 오픈 소스화
  - 2014년 11월 링크등니에서 Kafka를 개발하던 엔지니어들이 Kafka 개발에 집중하기 위해 Confluent라는 회사 설립
- 실시간 데이터 피드를 관리하기 위해 통일된 높은 처리량, 낮은 지연 시간을 지닌 플랫폼 제공

## 탄생 배경

- 모든 시스템으로 데이터를 실시간으로 전송하여 처리할 수 있는 시스템
- 데이터가 많아지더라도 확장이 용이한 시스템

## Kafka 데이터 처리 흐름

MySQL, Oracle, mongoDB, App, Storage => Kafka => Hadoop, Search Engine, Monitoring, Email

- Producer/Consumer 분리
- 메시지를 여러 Consumer에게 허용
- 높은 처리량을 위한 메시지 최적화
- Scale-out 가능
- Eco System

## Kafka Broker

- 실행 된 Kafka 애플리케이션 서버
- 3대 이상의 Broker Cluseter 구성 권장
- Zookeeper 연동
  - 역할: 메타데이터(Broker ID, Controller ID 등) 저장
  - Controller 정보 저장
- n개 Broker 중 1대는 Controller 기능 수행
  - Controller 역할
    - 각 Broker에게 담당 파티션 할당 수행
    - Broker 정상 동작 모니터링 관리

## Ecosystem (사용 시나리오) - 1. Kafka Client

- Kafka와 데이터를 주고받기 위해 사용하는 자바 라이브러리
- Producer, Consumer, Admin, Stream 등 Kafka 관련 API 제공
- 다양한 3rd party library 존재
- Kafka Cluster <-> Kafka-client Application

## 작동원리

- 메시지는 토픽으로 전달된다.
- 콘솔 파일을 기동할 때 토픽 이름을 명시한다.
- 전달하는 데이터는 토픽에 저장되고 그 데이터는 토픽에 관심있다는 Consumer가 받아간다.

### Zookeeper 및 Kafka 서버 구동

> .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

> .\bin\windows\kafka-server-start.bat .\config\server.properties

### Topic 생성

> .\bin\windows\kafka-topics.bat --create --topic quickstart-events --bootstrap-server localhost:9092 \ --partitions 1

### Topic 목록 확인

> .\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --list

### Topic 정보 확인

> .\bin\windows\kafka-topics.bat --describe --topic quickstart-events --bootstrap-server localhost:9092

### 메시지 생산 (Producer)

> .\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic quickstart-events

### 메시지 소비 (Consumer)

> .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic quickstart-events \ --from-beginning

> .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic my_topic_users \ --from-beginning

# Ecosystem 2. Kafka Connect

- Kafka Connect를 통해 Data를 Import/Export 가능
- 코드없이 Configuration으로 데이터를 이동
- Standalone mode, Distribution mode 지원
  - RESTful API 통해 지원
  - Stream 또는 Batch 형태로 데이터 전송 가능
  - 커스텀 Connector를 통한 다양한 Plugin 제공 (File, S3, Hive, Myql, etc ...)
- Source System (Hive, jdbc, ...) -> Kafka Connect Source -> Kafka Cluster -> Kafka Connect Sink -> Target System (S3, ...)

## Kafka Connect 실행

> .\bin\windows\connect-distributed.bat .\etc\kafka\connect-distributed.properties

# Tip

jpd: hibernate: ddl-auto: create-drop

> 초기데이터 저장 (SQL파일 이용해 바로 INSERT)

payload: 실제로 전달되는 데이터

{"schema":{"type":"struct","fields":[{"type":"int32","optional":false,"field":"id"},{"type":"string","optional":true,"field":"user_id"},{"type":"string","optional":true,"field":"pwd"},{"type":"string","optional":true,"field":"name"},{"type":"int64","optional":true,"name":"org.apache.kafka.connect.data.Timestamp","version":1,"field":"created_at"}],"optional":false,"name":"users"},"payload":{"id":6,"user_id":"admin2","pwd":"admin2","name":"super user2","created_at":1666789706000}}

- Kafka 메시지를 토픽에 전달해주는 역할: Source Connect

- 토픽데이터를 가져가서 사용하는 역할: Sink Connect

test
