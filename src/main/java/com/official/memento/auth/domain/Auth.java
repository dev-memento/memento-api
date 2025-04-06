package com.official.memento.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Auth {

    private Long id;
    private long memberId;
    private AuthProvider provider;
    private String platformId;
    private String refreshToken;
    private String fcmToken;

    private Auth(long memberId, AuthProvider provider, String platformId, String refreshToken) {
        this.memberId = memberId;
        this.provider = provider;
        this.platformId = platformId;
        this.refreshToken = refreshToken;
    }

    public static Auth withId(
            final Long id,
            final long memberId,
            final AuthProvider provider,
            final String platformId,
            final String refreshToken,
            final String fcmToken
    ) {
        return new Auth(id, memberId, provider, platformId, refreshToken,fcmToken);
    }

    public static Auth of(long memberId, AuthProvider provider, String platformId, String refreshToken) {
        return new Auth(memberId, provider, platformId, refreshToken);
    }

    public void withUpdatedToken(final String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }

    public Auth updateFcmToken(final String newRefreshToken) {
        return Auth.withId(
                this.id,
                this.memberId,
                this.provider,
                this.platformId,
                this.refreshToken,
                newRefreshToken
        );
    }
}