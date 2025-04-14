package com.example.crud_practice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

//@Profile({"local", "test"})
@Service
public class LocalFileStorageService implements FileStorageService {

    @Override
    public String storeFile(MultipartFile file, String directoryPath) throws IOException {
        // 1. 파일명 중복 방지를 위한 UUID 생성
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }

        String storedFileName = UUID.randomUUID() + "_" + originalFilename; // UUID로 파일명 중복 방지
        String savePath = directoryPath + File.separator + storedFileName;

        // 2. 실제 파일 저장
        try {
            file.transferTo(new File(savePath));
        } catch (IOException e) {
            throw new IOException("파일 저장 실패: " + originalFilename, e);
        }

        // 3. 저장된 파일 이름 반환
        return storedFileName;
    }
}
