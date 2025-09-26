package com.aksigorta.timesheet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Bu, /api/ altındaki tüm endpoint'ler için geçerli olacak
                .allowedOrigins("http://localhost:4200") // Sadece Angular uygulamasından gelen isteklere izin ver
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // İzin verilen HTTP metodları
                .allowedHeaders("*") // Tüm header'lara izin ver
                .allowCredentials(true);
    }
}