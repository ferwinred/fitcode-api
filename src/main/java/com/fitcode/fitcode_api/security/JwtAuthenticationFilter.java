package com.fitcode.fitcode_api.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (ExpiredJwtException e) {
                // invalid token
                sendUnauthorizedResponse(
                        response,
                        "TOKEN_EXPIRED",
                        "El token ha expirado. Debe autenticarse nuevamente.");
                return; // stop filter chain
            } catch (JwtException e) {
                // invalid token
                sendUnauthorizedResponse(
                        response,
                        "INVALID_TOKEN",
                        "El token es inválido. Debe autenticarse nuevamente.");
                return; // stop filter chain
            } catch (Exception e) {
                // other errors
                sendUnauthorizedResponse(
                        response,
                        "AUTH_ERROR",
                        "Error de autenticación. Debe autenticarse nuevamente.");
                return; // stop filter chain
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            boolean valid = jwtUtil.validateToken(token);

            if (valid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorizedResponse(
            HttpServletResponse response,
            String code,
            String message) throws IOException {

        response.resetBuffer();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json");

        response.getWriter().write("""
                {
                    "success": false,
                    "error": "%s",
                    "message": "%s"
                }
                """.formatted(code, message));

        response.flushBuffer();
    }
}
