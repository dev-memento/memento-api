package com.official.memento.auth.service;

import com.official.memento.auth.domain.AccessToken;
import com.official.memento.auth.domain.RefreshToken;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.MementoException;
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
    private static final long accessTokenExpiration = 604800000L;
    private static final long refreshTokenExpiration = 604800000L;

    public JwtUtil(@Value("${JWT.SECRET}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
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
            throw new MementoException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException exception) {
            exception.printStackTrace();
            throw new MementoException(ErrorCode.EXPECTED_TOKEN_ERROR);
        } catch (Exception exception) {
            throw new MementoException(ErrorCode.UNEXPECTED_TOKEN_ERROR);
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
            throw new MementoException(ErrorCode.INVALID_ID_TOKEN);
        }
    }
}
