package com.cw.cwu.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

// 이미지 파일 업로드 및 경로 생성 유틸리티 클래스

@Slf4j
@Component
public class FileUploadUtil {

    private static final String UPLOAD_DIR = "uploads/profiles";

    public String saveFile(MultipartFile file, String userId) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 저장할 수 없습니다.");
        }

        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalName = file.getOriginalFilename();

            if (originalName == null || !originalName.contains(".")) {
                throw new IllegalArgumentException("파일 이름이 올바르지 않거나 확장자가 없습니다.");
            }

            String ext = originalName.substring(originalName.lastIndexOf("."));
            String savedName = userId + "_profile" + ext;
            Path savePath = Paths.get(UPLOAD_DIR, savedName);

            file.transferTo(savePath);

            return "/uploads/profiles/" + savedName;

        } catch (IOException e) {
            log.error("파일 업로드 실패", e);
            throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

}
