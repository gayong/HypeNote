package com.surf.gateway.filter.global;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import javax.crypto.SecretKey;

@Component
@Slf4j
public class TokenValidationCheckFilter extends AbstractGatewayFilterFactory<TokenValidationCheckFilter.Config> {

    public TokenValidationCheckFilter() {
        super(Config.class);
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

                boolean isValidToken = validateToken(accessToken);
                if (!isValidToken) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
            }
            else {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info(String.valueOf(request.getHeaders()));
            }));
        });
    }

    public boolean validateToken(String accessToken) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

            Jwts.parser().decryptWith(secretKey).build().parseSignedClaims(accessToken);

            return true;

        } catch (JwtException e) {
            return false;
        }
    }

    public static class Config{}

}