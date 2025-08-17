package com.example.api_gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "jwt")
public record JwtProps(
        String secretFilePath,
        List<String> skipPaths
) {}