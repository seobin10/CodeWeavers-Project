package com.cw.cwu.security.filter;

import com.cw.cwu.domain.UserRole;
import com.cw.cwu.dto.UserDTO;
import com.cw.cwu.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        log.info("check uri....." + path);

        return request.getMethod().equals("OPTIONS")
                || path.startsWith("/api/user/login")
                || path.equals("/api/atelier/logout");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("----- JWTCheckFilter -----");

        String authHeaderStr = request.getHeader("Authorization");
        log.info("Authorization Header: {}", authHeaderStr);

        try {
            if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
            }

            String accessToken = authHeaderStr.substring(7).trim();
            if (accessToken.isEmpty()) {
                throw new IllegalArgumentException("토큰이 비어있습니다.");
            }

            Map<String, Object> claims = jwtUtil.getClaims(accessToken);
            log.info("JWT claims : {}", claims);

            Integer userId = parseUserId(claims.get("userId"));
            String email = (String) claims.get("email");
            String password = (String) claims.get("password");
            String phone = (String) claims.get("phone");
            String name = (String) claims.get("name");
            List<String> roleNames = extractRoleNames(claims.get("roleNames"));

            if (email == null || name == null || roleNames == null || roleNames.isEmpty()) {
                throw new IllegalArgumentException("필수 클레임이 누락되었습니다.");
            }

            UserRole role = UserRole.valueOf(roleNames.get(0).trim().toUpperCase(Locale.ROOT));

            UserDTO userDTO = new UserDTO(
                    String.valueOf(userId),
                    name,
                    email,
                    null,
                    phone,
                    role.name(),  // 그대로 유지
                    null
            );

            log.info("👤 인증된 사용자: {}", userDTO);

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.name());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password, List.of(authority));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("✅ 인증 완료, 필터 통과");

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("❌ JWT Check Error: {}", e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");

            String msg = new Gson().toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
            PrintWriter writer = response.getWriter();
            writer.print(msg);
            writer.close();
        }
    }

    private Integer parseUserId(Object userIdClaim) {
        if (userIdClaim instanceof Number) {
            return ((Number) userIdClaim).intValue();
        } else if (userIdClaim instanceof String) {
            return Integer.parseInt((String) userIdClaim);
        } else {
            throw new IllegalArgumentException("userId 클레임이 잘못되었습니다.");
        }
    }

    private List<String> extractRoleNames(Object roleNamesClaim) {
        if (roleNamesClaim instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        } else if (roleNamesClaim instanceof String str) {
            return List.of(str);
        } else if (roleNamesClaim instanceof UserRole role) {
            return List.of(role.name());
        } else {
            throw new IllegalArgumentException("유효하지 않은 roleNames claim 타입: " + roleNamesClaim);
        }
    }
}
