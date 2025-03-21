package com.official.memento.auth.infrastructure.google;

public record GoogleTokenResponse(

        String accessToken,

        int expiresIn,

        String scope,
        String tokenType
) {
}