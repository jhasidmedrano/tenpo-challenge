package com.tenpo.history.config;

import com.tenpo.history.security.CustomAuthenticationEntryPoint;
import com.tenpo.history.security.TokenAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomAuthenticationEntryPoint entryPoint;

    public SecurityConfig(JwtUtil jwtUtil, CustomAuthenticationEntryPoint entryPoint) {
        this.jwtUtil = jwtUtil;
        this.entryPoint = entryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/1/history/health").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
                .addFilterBefore(new TokenAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
