package com.example.demo.common.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app.security")
@Data

public class PublicPathsConfig {
    private List<String> publicPaths;
}
