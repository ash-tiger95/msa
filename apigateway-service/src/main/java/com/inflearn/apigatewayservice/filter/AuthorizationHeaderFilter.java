package com.inflearn.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> { // 권한 필터
    Environment env;

    public AuthorizationHeaderFilter(Environment env){
        // 새롭게 추가되는 필터 클래스에서 Config 정보가 아무것도 없지만, Config 정보를 필터에 적용하기 위해
        // 캐스팅하는 작업을 부모클래스에 알려줘야한다. =>  super(Config.class);
        super(Config.class);
        this.env = env;
    }

    public static class Config{

    }



    // API 호출할 때 Header에 토큰이 제대로 발급된 것인지 체크하는 작업
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Header에 토큰정보가 있는지 검사
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "no authorization header",HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0); // Bearer
            String jwt = authorizationHeader.replace("Bearer",""); // 토큰 불러오기 (Bearer를 제외한 부분이 토큰)

            // 유요한 토큰인지 검사
            if(!isJwtValid(jwt)){ 
                return onError(exchange, "JWT token is not valid",HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        });
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;
        try{
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret")) // 복호화
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
        }catch (Exception e){
            returnValue = false;
        }

        if(subject == null || subject.isEmpty()){
            returnValue = false;
        }



        return returnValue;
    }

    // Spring Gateway Service는 Spring MVC가 아닌(HttpServletRequest, HttpServletResponse) Spring WebFlux, 데이터 처리는 비동기 방식
    // Mono(단일값), Flux(단중값) -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse(); // ServletResponse가 아닌 ServerHttpResponse
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete(); // setComplete() 모노타입으로 전달
    }
}
