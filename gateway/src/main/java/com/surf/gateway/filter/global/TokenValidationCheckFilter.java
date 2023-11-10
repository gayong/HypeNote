package com.surf.gateway.filter.global;

import com.surf.gateway.authenticator.TokenAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TokenValidationCheckFilter extends AbstractGatewayFilterFactory<TokenValidationCheckFilter.Config> {

    private final TokenAuthenticator tokenAuthenticator;

    public TokenValidationCheckFilter(TokenAuthenticator tokenAuthenticator) {
        super(Config.class);
        this.tokenAuthenticator = tokenAuthenticator;
    }

    @Value("${jwt.secret}")
    private String SECRET;

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (request.getHeaders().containsKey("accessToken")) {
                String accessToken = request.getHeaders().getFirst("accessToken");

                boolean isValidToken = tokenAuthenticator.validateToken(accessToken);
                if (!isValidToken) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                } else {
                    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        log.info(String.valueOf(request.getHeaders()));
                    }));
                }
            }
            else {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }
        });
    }

    public static class Config{}



}