package com.tenpo.history.security;

import com.tenpo.history.config.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public TokenAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            String errorMsg = "Missing or invalid Authorization header";
            request.setAttribute("auth_error_message", errorMsg);
            throw new InsufficientAuthenticationException(errorMsg);
        }

        String token = authHeader.substring(7);

        try {
            jwtUtil.validateToken(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(null, null, List.of());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            String errorMsg = "Invalid or expired token";
            request.setAttribute("auth_error_message", errorMsg);
            throw new InsufficientAuthenticationException(errorMsg);
        }
    }

}