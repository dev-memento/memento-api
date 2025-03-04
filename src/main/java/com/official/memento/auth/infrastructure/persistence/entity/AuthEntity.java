package com.official.memento.auth.infrastructure.persistence.entity;

import com.official.memento.auth.domain.Auth;
import com.official.memento.auth.domain.AuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"platformId", "provider"})})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long memberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false)
    private String platformId;

    @Column(nullable = false)
    private String refreshToken;

    public static AuthEntity of(final Auth auth) {
        return new AuthEntity(auth.getId(), auth.getMemberId(), auth.getProvider(), auth.getPlatformId(), auth.getRefreshToken());
    }

    public Long getId() {
        return id;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getMemberId() {
        return memberId;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }
}
