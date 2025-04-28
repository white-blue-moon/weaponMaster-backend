package com.example.weaponMaster.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 설정
                .allowedOrigins("http://localhost:8080")  // 허용할 클라이언트 도메인 -> TODO config 등으로 경로 받아오도록 수정 필요
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")  // 허용할 HTTP 메서드
                .allowedHeaders("*")  // 허용할 헤더
                .allowCredentials(true);  // 자격 증명 허용
    }
}
