package com.official.memento.auth.service;

import com.official.memento.auth.domain.AccessToken;
import com.official.memento.auth.domain.RefreshToken;
import com.official.memento.global.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(
            @Value("${JWT.SECRET}") String secretKey,
            @Value("${JWT.ACCESS_TOKEN_EXPIRATION}") long accessTokenExpiration,
            @Value("${JWT.REFRESH_TOKEN_EXPIRATION}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public AccessToken generateAccessToken(final Long userId) {
        return new AccessToken(Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact());
    }

    public RefreshToken generateRefreshToken(final Long userId) {
        return new RefreshToken(Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact());
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException exception) {
            throw new ExpiredTokenException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException exception) {
            throw new InvalidJwtTokenException(ErrorCode.EXPECTED_TOKEN_ERROR);
        } catch (Exception exception) {
            throw new UnexpectedTokenException(ErrorCode.UNEXPECTED_TOKEN_ERROR);
        }
    }

    public String getUserIdFromToken(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new InvalidIdTokenException(ErrorCode.INVALID_ID_TOKEN);
        }
    }
}
