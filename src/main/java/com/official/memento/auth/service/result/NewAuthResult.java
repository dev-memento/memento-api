package com.official.memento.auth.service.result;

public record NewAuthResult(
        String accessToken,
        String refreshToken,
        boolean isNewUser
) {
    public static NewAuthResult of(final String accessToken, final String refreshToken, final boolean isNewUser) {
        return new NewAuthResult(accessToken, refreshToken, isNewUser);
    }
}
