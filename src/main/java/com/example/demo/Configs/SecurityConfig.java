package com.example.demo.Configs;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.multipart.support.MultipartFilter;

import com.example.demo.Security.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CorsConfigurationSource corsConfigurationSource;
	private final JwtFilter jwtFilter;
	private final RateLimitingFilter rateLimitingFilter;

	public SecurityConfig(CorsConfigurationSource corsConfigurationSource, JwtFilter jwtFilter,
			RateLimitingFilter rateLimitingFilter) {
		this.corsConfigurationSource = corsConfigurationSource;
		this.jwtFilter = jwtFilter;
		this.rateLimitingFilter = rateLimitingFilter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/api/auth/register", "/api/auth/login").permitAll().anyRequest()
						.authenticated())

				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(rateLimitingFilter, JwtFilter.class);

		return http.build();
	}

	@Bean
	public FilterRegistrationBean<MultipartFilter> multipartFilter() {
		FilterRegistrationBean<MultipartFilter> registrationBean = new FilterRegistrationBean<>(new MultipartFilter());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}
}
