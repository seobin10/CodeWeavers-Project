package com.cw.cwu.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {

    @Value("${JWT_SECRET}") // .env에 있는 값 읽기
    private String secretKeyString;

    private SecretKey secretKey;


    private final long expireMs = 1000 * 60 * 60 * 1; // 1시간

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKeyString.getBytes()));
    }

    // JWT 생성: userId를 subject로 사용
    public String generateToken(String userId, Map<String, Object> claims, int minutes) {
        claims.put("userId", userId); // ✨ 반드시 추가
        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(minutes).toInstant()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> getClaims(String token) {
        log.info("📨 getClaims() - 전달된 토큰: {}", token);
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    public String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return (String) claims.get("userId");
        } catch (Exception e) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }

    public String getUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 제거
            return getUserIdFromToken(token);
        }

        throw new RuntimeException("Authorization 헤더가 없거나 올바르지 않습니다.");
    }

}