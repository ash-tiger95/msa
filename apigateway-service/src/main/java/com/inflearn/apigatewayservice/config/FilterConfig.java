package com.inflearn.apigatewayservice.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 스프링부트 최초 실행일 때 Configuration 빈만 모아서 메모리에 등록한다.
public class FilterConfig {

    // yml에서 말고 java로 라우팅 처리
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.path("/first-service/**") // 1. 요청이 들어오면
                            .filters(f -> f.addRequestHeader("first-request","first-request-header")
                                        .addResponseHeader("first-response","first-response-header")) // request/response 필터 등록 가능
                            .uri("http://localhost:8081/") // 2. 여기로 이동한다.
                ) // 0. yml 라우팅 작업을 자바에서 할 수 있다.
                .route(r -> r.path("/second-service/**")
                        .filters(f -> f.addRequestHeader("second-request","second-request-header")
                                .addResponseHeader("second-response","second-response-header"))
                        .uri("http://localhost:8082/")
                )
                .build(); // 0. 라우트에 필요한 정보를 메모리에 반영
    }
}
