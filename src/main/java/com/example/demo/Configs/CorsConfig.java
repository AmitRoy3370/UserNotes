package com.example.demo.Configs;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Replace "*" with your Flutter app's actual origin(s) in production
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));

        // Explicitly allow PUT and other methods you need
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Allow all headers (including Authorization for your JWT)
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        // If you use cookies, set this to true, but with "*" origin it's not allowed.
        // For a JWT-based API, false is usually fine.
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
