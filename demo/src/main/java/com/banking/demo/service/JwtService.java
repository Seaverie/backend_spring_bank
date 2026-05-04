package com.banking.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expire}")
    private int expire;
    @Value("${jwt.refresh_expire}")
    private int refresh_expire;

    private String generateToken(Map<String, Object> claims, UserDetails userDetails, long expiresIn) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiresIn))
                .signWith(getSigningKey(),SignatureAlgorithm.HS512)
                .compact();
    }
    private Key getSigningKey() {
        byte[] secretBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(secretBytes);
    }

    private Claims extractAllClaims(String token) {
        
    }

}
