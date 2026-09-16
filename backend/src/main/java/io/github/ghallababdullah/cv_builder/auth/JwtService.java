package io.github.ghallababdullah.cv_builder.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtService {
    private final SecretKey secretKey ;
    private final long expirationMs ;

    public JwtService(
            @Value("${app.jwt.secret}") String secret ,
            @Value("${app.jwt.expirationMs}") long expirationMs
    ){
        this.secretKey= Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs=expirationMs ;
    }

    /**
     * Генерирует JWT токен для пользователя.
     */

    //Claims — данные которые кладём в payload токена. Каждый claim — пара ключ-значение.

    public String generateToken(Long userId, String email){

        Map<String , Object> claims = new HashMap<>();

        claims.put("userId",userId);

        Date now = new Date() ;

        Date expiration = new Date(now.getTime() + expirationMs);

        String token = Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();

        log.debug("Generated JWT for user {} (expires at {})", email, expiration);
        return token ;
    }

    /**
     * Извлекает email (subject) из токена.
     * Также валидирует подпись и expiration.
     */

    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();

    }

    /**
     * Извлекает userId из токена.
     */
    public Long extractUserId(String token){
        Claims claims = extractAllClaims(token);
        return  claims.get("userId", Long.class);
    }

    /**
     * Проверяет валидность токена (подпись + expiration).
     * Возвращает true/false, не бросает исключения.
     */
    public boolean isValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }



    /**
     * Внутренний метод: парсинг токена с валидацией подписи.
     * Бросает исключение если токен невалиден.
     */

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload() ;
    }




}
