package com.official.memento.auth.service.usecase;

import com.official.memento.auth.service.AuthResult;

@FunctionalInterface
public interface RefreshTokenUseCase {
    AuthResult refreshTokens(String refreshToken);
}
