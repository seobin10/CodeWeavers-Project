package com.cw.cwu.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRequestUtil {

    private final JWTUtil jwtUtil;

    public String extractUserId(HttpServletRequest request) {
        return jwtUtil.getUserIdFromRequest(request);
    }
}