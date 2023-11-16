package com.surf.gateway.filter.global;

import com.surf.gateway.authenticator.TokenAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@Slf4j
public class TokenValidationCheckFilter extends AbstractGatewayFilterFactory<TokenValidationCheckFilter.Config> {

    private final TokenAuthenticator tokenAuthenticator;

    public TokenValidationCheckFilter(TokenAuthenticator tokenAuthenticator) {
        super(Config.class);
        this.tokenAuthenticator = tokenAuthenticator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (request.getHeaders().containsKey("Authorization")) {
                String accessToken = Objects.requireNonNull(request.getHeaders().getFirst("Authorization")).substring(7);

                boolean isValidToken = tokenAuthenticator.validateToken(accessToken);
                if (!isValidToken) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    DataBuffer buffer = response.bufferFactory().wrap("유효하지 않은 토큰입니다.".getBytes(StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(buffer));
                } else {
                    return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                        log.info(String.valueOf(request.getHeaders()));
                    }));
                }
            }
            else {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                DataBuffer buffer = response.bufferFactory().wrap("토큰이 존재하지 않습니다.".getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer));
            }
        });
    }

    public static class Config{}



}