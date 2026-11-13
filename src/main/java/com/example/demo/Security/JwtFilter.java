package com.example.demo.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.Models.UserModels.User;
import com.example.demo.Repositories.UserRepositories.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== JwtFilter hit: " + request.getMethod() + " " + request.getRequestURI() + " ===");

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // ✅ Use userId from `sub` — the reliable, immutable identity
            String userId = jwtUtil.extractUserId(token);
            String username = jwtUtil.extractUsername(token);

            System.out.println("Token parsed → userId = " + userId + " | username = " + username);

            if (userId != null
                    && SecurityContextHolder.getContext().getAuthentication() == null
                    && jwtUtil.validateToken(token)) {

                // ✅ Look up by primary key (fast, unique)
                Optional<User> userOptional = userRepository.findById(userId);

                System.out.println("User found in DB: " + userOptional.isPresent());

                if (userOptional.isPresent()) {
                    User user = userOptional.get();

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER")));

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("✅ AUTH SET for user: " + user.getUserName()
                            + " | principal = " + user.getId()
                            + " | authorities = " + authToken.getAuthorities());
                }
            } else {
                System.out.println("Skipped auth: userId=" + userId
                        + " | contextClear=" + (SecurityContextHolder.getContext().getAuthentication() == null)
                        + " | tokenValid=" + jwtUtil.validateToken(token));
            }

        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {

            System.out.println("❌ JWT error: " + e.getClass().getSimpleName() + " - " + e.getMessage());

            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"error\":\"Invalid or expired JWT token\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
