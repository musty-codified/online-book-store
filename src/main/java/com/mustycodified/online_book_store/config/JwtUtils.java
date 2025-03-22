package com.mustycodified.online_book_store.config;


import com.mustycodified.online_book_store.entity.User;
import com.mustycodified.online_book_store.exception.CustomValidationException;
import com.mustycodified.online_book_store.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final UserRepository userRepository;
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    public Claims getAllClaimsFromToken(String token) {
        try {

            token = token.trim();
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception  e){
            throw new RuntimeException("Failed to parse token: " + e.getMessage(), e);

        }
    }
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }
    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }
    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }
    public String generateToken(UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new CustomValidationException("Error generating token"));
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().stream()
                .map(Objects::toString).collect(Collectors.joining(",")));
        claims.put("email", userDetails.getUsername());
        claims.put("userId", user.getId());

        String token = doGenerateToken(claims, userDetails.getUsername(), jwtExpiration);
        System.out.println("JWT Token: " + token);
        return doGenerateToken(claims, userDetails.getUsername(), jwtExpiration);

    }

    private String doGenerateToken(Map<String, Object> claims, String email, Long jwtExpiration) {
        Long expirationTimeSeconds = jwtExpiration;
        Long expirationTimeMillis = expirationTimeSeconds * 1000 ;

        final Date createdDate = new Date(System.currentTimeMillis());
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
}
