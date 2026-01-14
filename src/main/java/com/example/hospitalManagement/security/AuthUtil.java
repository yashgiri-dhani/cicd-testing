package com.example.hospitalManagement.security;

import com.example.hospitalManagement.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private  String jwtSecretKey ;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {

        return Jwts.builder()
                .setSubject(user.getUsername())                       // token kis user ke liye hai
                .claim("id", user.getId())                            // custom claim
                .setIssuedAt(new Date(System.currentTimeMillis()))    // issue time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)     // signing HS256
                .compact();
    }

    public String getUsernameFromToken(String token) {

        Claims claims =  Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject() ;

    }
}
