package com.official.memento.auth.infrastructure.persistence.entity;

import com.official.memento.auth.domain.Auth;
import com.official.memento.auth.domain.AuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"platformId", "provider"})})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    @Column(nullable = false)
    private String fcmToken;

    public static AuthEntity of(final Auth auth) {
        return new AuthEntity(
                auth.getId(),
                auth.getMemberId(),
                auth.getProvider(),
                auth.getPlatformId(),
                auth.getRefreshToken(),
                auth.getFcmToken()
        );
    }

}
