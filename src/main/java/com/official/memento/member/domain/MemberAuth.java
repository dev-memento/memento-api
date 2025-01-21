package com.official.memento.member.domain;

import com.official.memento.auth.domain.AuthProvider;

public class MemberAuth {
    private Long id;
    private long memberId;
    private AuthProvider provider;
    private String platformId;
    private String refreshToken;

    private MemberAuth(Long id, long memberId, AuthProvider provider, String platformId, String refreshToken) {
        this.id = id;
        this.memberId = memberId;
        this.provider = provider;
        this.platformId = platformId;
        this.refreshToken = refreshToken;
    }

    private MemberAuth(long memberId, AuthProvider provider, String platformId, String refreshToken) {
        this.memberId = memberId;
        this.provider = provider;
        this.platformId = platformId;
        this.refreshToken = refreshToken;
    }

    public static MemberAuth withId(Long id, long memberId, AuthProvider provider, String platformId, String refreshToken) {
        return new MemberAuth(id, memberId, provider, platformId, refreshToken);
    }

    public static MemberAuth of(long memberId, AuthProvider provider, String platformId, String refreshToken) {
        return new MemberAuth(memberId, provider, platformId, refreshToken);
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