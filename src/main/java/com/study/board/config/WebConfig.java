package com.study.board.config;  // 패키지 위치가 다를 수도 있음 (꼭 확인!)

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**")  // 클라이언트가 접근할 URL 패턴
                .addResourceLocations("classpath:/static/files/"); // 실제 파일 경로
    }
}
