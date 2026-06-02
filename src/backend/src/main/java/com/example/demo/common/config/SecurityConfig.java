package com.example.demo.common.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.auth.filter.JwtFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler()))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**", "/h2-console/**", "/verification/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/public/agencies/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/public/vehicles/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("SUPER_ADMIN")
                    .requestMatchers(HttpMethod.POST, "/agencies/**").hasAnyRole("SUPER_ADMIN", "AGENCY_MANAGER")
                    .requestMatchers(HttpMethod.PUT, "/agencies/**").hasAnyRole("SUPER_ADMIN", "AGENCY_MANAGER")
                    .requestMatchers(HttpMethod.PATCH, "/agencies/**").hasAnyRole("SUPER_ADMIN", "AGENCY_MANAGER")
                    .requestMatchers(HttpMethod.DELETE, "/agencies/**").hasAnyRole("SUPER_ADMIN", "AGENCY_MANAGER")
                    .requestMatchers("/agencies/*/vehicles/**").hasAnyRole("SUPER_ADMIN", "AGENCY_MANAGER")
                    .requestMatchers("/agencies/*/bookings/**").hasAnyRole("SUPER_ADMIN", "AGENCY_MANAGER")
                    .requestMatchers("/bookings/my/**").hasAnyRole("CLIENT", "SUPER_ADMIN")
                    .requestMatchers(HttpMethod.GET, "/agencies/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/vehicles/**").authenticated()
                    .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> writeJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }

    private void writeJsonError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}