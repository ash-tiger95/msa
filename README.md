# MSA

Study MSA used SpringBoot

# 프로젝트 에러

1 error
role: org.apache.maven.model.validation.ModelValidator
roleHint: ide

> File > Settings > Build, Execution, Deployment > Build Tools > Maven > Maven home path 설정

# Spring Cloud Netflix Eureka

역할: 요청정보에 따른 마이크로서비스 위치를 알려준다.

dependency: Eureks Server

@SpringBootApplication: main함수 역할, 스프링부트는 가지고 있는 모든 클래스 중 이 어노테이션을 검색하고 실행
@EnableEurekaServer: service discovery 서버 등록
