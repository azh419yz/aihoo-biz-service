package com.aihoo.api.doctor.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.aihoo.api.doctor.config.security.PublicEndpoints.PUBLIC_URLS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    public SecurityConfig(TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auths -> auths
                        .requestMatchers(PUBLIC_URLS)
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
