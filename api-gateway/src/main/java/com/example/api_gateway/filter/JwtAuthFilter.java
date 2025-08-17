package com.example.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher matcher = new AntPathMatcher();
    private final SecretKey key;
    private final String[] skipPatterns;

    public JwtAuthFilter(
            @Value("${jwt.secret-file-path}") String secretFilePath,
            @Value("${jwt.skip-paths:/api/auth/**,/actuator/health,/actuator/info}") String skipCsv
    ) {
        this.key = loadKeyFromBase64File(secretFilePath);
        this.skipPatterns = Arrays.stream(skipCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);
    }

    private SecretKey loadKeyFromBase64File(String pathStr) {
        try {
            String base64 = Files.readString(Path.of(pathStr)).trim(); // file holds Base64 text
            byte[] bytes = Decoders.BASE64.decode(base64);
            if (bytes.length < 32) throw new IllegalStateException("JWT secret too short (<32 bytes).");
            return Keys.hmacShaKeyFor(bytes);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read JWT secret from " + pathStr + ": " + e, e);
        }
    }

    private boolean shouldSkip(String path) {
        for (String p : skipPatterns) if (matcher.match(p, path)) return true;
        return false;
    }

    private static String bearer(String auth) {
        return (auth != null && auth.regionMatches(true,0,"Bearer ",0,7))
                ? auth.substring(7).trim() : null;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var req = exchange.getRequest();
        var path = req.getPath().value();

        if (shouldSkip(path)) {
            exchange.getResponse().getHeaders().add("X-Gateway-Auth", "SKIPPED");
            return chain.filter(exchange);
        }

        String token = bearer(req.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        if (token == null || token.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            Claims claims = Jwts.parser()
                    .clockSkewSeconds(60)
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            var mutated = req.mutate()
                    .header("X-User", claims.getSubject() == null ? "" : claims.getSubject())
                    .header("X-Role", claims.get("role", String.class))
                    .build();

            return chain.filter(exchange.mutate().request(mutated).build());

        } catch (JwtException e) {
            exchange.getResponse().getHeaders().add("X-Gateway-Auth-Error", e.getClass().getSimpleName());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override public int getOrder() { return -1; }
}
