package com.inflearn.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Global Filter baseMessage: request id -> {} ", config.getBaseMessage());
//
//            if(config.isPreLogger()){ // PreLogger가 작동되어야 한다면
//                log.info("Global Filter Start: request id -> {}",request.getId());
//            }
//
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                if(config.isPostLogger()){// PostLogger가 작동되어야 한다면
//                    log.info("Global Filter End: response id -> {}",response.getStatusCode());
//                }
//            }));
//        });
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            // WebFlux 2.0
            // exchange: Servlet Request/Response 역할(더 이상 지원 x), Server Request/Response
            // chain: pre/post chain

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage: request id -> {} ", config.getBaseMessage());

            if(config.isPreLogger()){ // PreLogger가 작동되어야 한다면
                log.info("Logging PRE Filter: request id -> {}",request.getId());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.isPostLogger()){// PostLogger가 작동되어야 한다면
                    log.info("Logging POST Filter: response id -> {}",response.getStatusCode());
                }
            }));
        }, Ordered.LOWEST_PRECEDENCE // 필터 우선순위, Ordered.HIGHEST_PRECEDENCE: 가장 먼저/Ordered.LOWEST_PRECEDENCE: 가장 나중에
        ); 

        return filter;
    }

    @Data
    public static class Config{
        // Put the configuration properties (.yml에서 불러와서 set한다)
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
