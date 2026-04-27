package com.fitcode.fitcode_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.security.Key;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMs;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs) {
        // conviene un secret largo; Keys.hmacShaKeyFor espera bytes

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
        System.out.println("JWT:" + key + " Key created with secret of length " + secret.length());
        System.out.println("JWT:" + expirationMs + " ms expiration.");

    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());
        claims.put("roles", roles);

        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Object r = claims.get("roles");
        if (r instanceof List) {
            return ((List<?>) r).stream().map(Object::toString).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
