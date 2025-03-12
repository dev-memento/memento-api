package com.official.memento.auth.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthResultDto {
    private final String accessToken;
    private final String refreshToken;

    public static AuthResultDto of(
            String accessToken,
            String refreshToken) {
        return new AuthResultDto(accessToken, refreshToken);
    }
}