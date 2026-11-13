package com.example.demo.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Security.JwtFilter;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Services.SecurityService.BucketService;

@Configuration
public class RateLimitConfig {

    @Bean
    public RateLimitingFilter rateLimitingFilter(BucketService bucketService) {
        return new RateLimitingFilter(bucketService);
    }

    @Bean
    public JwtFilter jwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        return new JwtFilter(jwtUtil, userRepository);
    }
}
