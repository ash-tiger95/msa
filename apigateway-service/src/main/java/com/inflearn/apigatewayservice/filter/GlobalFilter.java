package com.inflearn.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter baseMessage: request id -> {} ", config.getBaseMessage());

            if(config.isPreLogger()){ // PreLogger가 작동되어야 한다면
                log.info("Global Filter Start: request id -> {}",request.getId());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.isPostLogger()){// PostLogger가 작동되어야 한다면
                    log.info("Global Filter End: response id -> {}",response.getStatusCode());
                }
            }));
        });
    }

    @Data
    public static class Config{
        // Put the configuration properties (.yml에서 불러와서 set한다)
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
