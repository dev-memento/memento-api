package com.official.memento.auth.service.usecase;

import com.official.memento.auth.service.result.AuthResult;

@FunctionalInterface
public interface RefreshTokenUseCase {
    AuthResult refreshTokens(final String authorizationHeader);
}
