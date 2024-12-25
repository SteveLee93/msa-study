package com.example.auth_service.config;

import java.security.Key;
import java.util.Date;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
  @Value("${jwt.secret}")
  private String secretKey;

  private Key key;
  private final long validityInMilliseconds = 3600000; // 1h

  @PostConstruct
  protected void init() {
    byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(String username) {
    Claims claims = Jwts.claims().setSubject(username);
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(key)
        .compact();
  }

  public String getEmailFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public long getExpirationTime() {
    return this.validityInMilliseconds;
  }
}