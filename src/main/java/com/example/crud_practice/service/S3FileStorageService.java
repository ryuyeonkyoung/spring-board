package com.example.crud_practice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3FileStorageService implements FileStorageService {
    @Override
    public String storeFile(MultipartFile file, String directoryPath) throws IOException {
        return "";
    }
}
