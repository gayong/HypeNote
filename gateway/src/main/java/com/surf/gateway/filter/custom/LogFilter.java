package com.surf.gateway.filter.custom;

import com.surf.gateway.filter.global.TokenValidationCheckFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LogFilter extends AbstractGatewayFilterFactory<TokenValidationCheckFilter.Config> {


    @Override
    public GatewayFilter apply(TokenValidationCheckFilter.Config config) {
        return ((exchange, chain) -> {

            log.info("보내지나");


            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("나갈 때 되나");
            }));
        });
    }

    public static class Config{}
}
