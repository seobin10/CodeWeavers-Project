package com.cw.cwu.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
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
    private final SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode("yourSecretKeyYourSecretKey".getBytes()));
    private final long expireMs = 1000 * 60 * 60 * 1; // 1시간

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
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

//    // ✅ 로그인 성공 후 토큰 생성용 메서드
//    public static String createToken(User user) {
//        Map<String, Object> valueMap = new HashMap<>();
//        valueMap.put("userId", user.getUserId());
//        valueMap.put("email", user.getUserEmail());
//        valueMap.put("name", user.getName());
//        valueMap.put("phone", user.getUserPhone());
//        valueMap.put("roleNames", user.getUserRole().name()); // enum
//
//        JWTUtil jwtUtil = new JWTUtil();
//        return jwtUtil.generateToken(user.getUserId(), valueMap, 60); // 60분 유효
//    }
}