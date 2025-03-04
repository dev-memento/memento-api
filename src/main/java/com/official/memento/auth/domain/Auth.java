package com.official.memento.auth.domain;

public class Auth {

    private Long id;
    private long memberId;
    private AuthProvider provider;
    private String platformId;
    private String refreshToken;

    private Auth(Long id, long memberId, AuthProvider provider, String platformId, String refreshToken) {
        this.id = id;
        this.memberId = memberId;
        this.provider = provider;
        this.platformId = platformId;
        this.refreshToken = refreshToken;
    }

    private Auth(long memberId, AuthProvider provider, String platformId, String refreshToken) {
        this.memberId = memberId;
        this.provider = provider;
        this.platformId = platformId;
        this.refreshToken = refreshToken;
    }

    public static Auth withId(Long id, long memberId, AuthProvider provider, String platformId, String refreshToken) {
        return new Auth(id, memberId, provider, platformId, refreshToken);
    }

    public static Auth of(long memberId, AuthProvider provider, String platformId, String refreshToken) {
        return new Auth(memberId, provider, platformId, refreshToken);
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
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

    public void withUpdatedToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }
}