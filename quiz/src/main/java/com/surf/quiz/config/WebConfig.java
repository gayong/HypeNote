//package com.surf.quiz.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000", "https://k9e101.p.ssafy.io", "http://k9e101.p.ssafy.io", "https://www.hype-note.com", "http://www.hype-note.com")
//                .allowedMethods("*")
//                .allowCredentials(true);
//    }
//}