package com.example.demo.Security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String secret =
        "MoSCoooooW-Waa-MoSCooW-Barcaaaaaa-Waa-Barcaa";

    private final SecretKey key =
        Keys.hmacShaKeyFor(secret.getBytes());

    public String generateAccessToken(UserDetails user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .claim(
                    "roles", user.getAuthorities()
                                       .stream()
                                       .map(GrantedAuthority::getAuthority)
                                       .toList()
                )
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {

        return parseToken(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenExpired(String token) {

        return parseToken(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public Jws<Claims> parseToken(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}
