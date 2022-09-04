package com.inflearn.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE Filter: request id -> {} ", request.getId());

            // Custom Post Filter
            // chain: 반환 연결
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { // Mono(Web Flux): 비동기 방식에서 단일값 전달
                log.info("Custom POST Filter: response id -> {} ", response.getStatusCode());
            }));
        });
    }

    public static class Config{
        // Put the configuration properties
    }
}
