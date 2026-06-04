package com.example.demo.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class CorsConfig implements WebMvcConfigurer {

//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")  // Applique à tous les endpoints
//                 .allowedOrigins("*")  // Autorise toutes les origines
//                 .allowedMethods("*")  // Autorise toutes les méthodes HTTP (GET, POST, PUT, DELETE, etc.)
//                 .allowedHeaders("*")  // Autorise tous les headers
//                 .exposedHeaders("*")  // Expose tous les headers
//                 .allowCredentials(false)  // IMPORTANT: doit être false car allowedOrigins = "*"
//                 .maxAge(3600);  // Cache la réponse preflight pendant 1 heure
//     }
// }
