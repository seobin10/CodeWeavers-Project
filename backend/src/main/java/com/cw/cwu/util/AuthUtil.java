package com.cw.cwu.util;

import org.springframework.security.access.AccessDeniedException;

// 권한 검사
public class AuthUtil {
    public static void checkOwnership(String ownerId, String requesterId) throws AccessDeniedException {
        if (!ownerId.equals(requesterId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }
}
