package com.sejus.sigeplan.infrastructure.security;

import com.sejus.sigeplan.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.expiration}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    public String generateToken(User user) {
        Instant now = Instant.now();

        List<String> roles = user.roles().stream()
                .map(role -> role.name())
                .distinct()
                .toList();

        List<String> permissions = user.roles().stream()
                .flatMap(role -> role.permissions().stream())
                .map(permission -> permission.name())
                .distinct()
                .toList();

        return Jwts.builder()
                .subject(user.cpf())
                .claims(Map.of(
                        "uid", user.id().toString(),
                        "cpf", user.cpf(),
                        "email", user.email(),
                        "name", user.fullName(),
                        "roles", roles,
                        "permissions", permissions
                ))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expiration)))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, AuthenticatedUser user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    public long getExpirationInSeconds() {
        return expiration / 1000;
    }

    public Set<String> extractRoles(String token) {
        Object roles = extractAllClaims(token).get("roles");

        if (roles instanceof List<?> roleList) {
            return roleList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
        }

        return Set.of();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}