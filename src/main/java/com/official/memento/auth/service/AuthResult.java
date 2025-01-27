package com.official.memento.auth.service;

import com.official.memento.auth.domain.AccessToken;
import com.official.memento.auth.domain.RefreshToken;

public class AuthResult {
    private final AccessToken accessToken;
    private final RefreshToken refreshToken;
    private final boolean isNewUser;

    private AuthResult(AccessToken accessToken, RefreshToken refreshToken, boolean isNewUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isNewUser = isNewUser;
    }
    
    public static AuthResult of(
            AccessToken accessToken,
            RefreshToken refreshToken,
            boolean isNewUser
    ) {
        return new AuthResult(accessToken, refreshToken, isNewUser);
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public boolean isNewUser() {
        return isNewUser;
    }
}