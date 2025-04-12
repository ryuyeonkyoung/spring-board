package com.example.crud_practice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Getter
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String resourcePath = "/upload/**"; // view 에서 접근할 경로
    @Value("${file.upload-dir}") // 외부 설정에서 경로 주입
    private String savePath; // 실제 파일 저장 경로(win)

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations("file:" + savePath + "/");
    }
}