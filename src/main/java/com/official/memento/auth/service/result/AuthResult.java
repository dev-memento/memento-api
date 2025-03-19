package com.official.memento.auth.service.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthResult {
    private final String accessToken;
    private final String refreshToken;

    public static AuthResult of(
            String accessToken,
            String refreshToken) {
        return new AuthResult(accessToken, refreshToken);
    }
}