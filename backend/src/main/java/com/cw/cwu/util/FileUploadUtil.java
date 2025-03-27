package com.cw.cwu.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

// 이미지 파일 업로드 및 경로 생성 유틸리티 클래스

@Slf4j
@Component
public class FileUploadUtil {

    private static final String UPLOAD_DIR = "uploads/profiles";

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 저장할 수 없습니다.");
        }

        try {
            // 저장 폴더 생성
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 파일명 생성 (UUID + 원본 파일명)
            String originalName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String savedName = uuid + "_" + originalName;

            // 저장 경로
            Path savePath = Paths.get(UPLOAD_DIR, savedName);

            // 파일 저장
            file.transferTo(savePath);

            // URL 경로 반환
            return "/uploads/profiles/" + savedName;

        } catch (IOException e) {
            log.error("파일 업로드 실패", e);
            throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

}
