package com.fashionstore.fashionstore.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
    private final String SECRET_KEY = "mysecretkey"; // nên để trong application.properties

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}

