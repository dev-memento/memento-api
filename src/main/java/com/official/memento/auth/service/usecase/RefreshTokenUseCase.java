package com.official.memento.auth.service.usecase;

import com.official.memento.auth.service.AuthResultDto;

@FunctionalInterface
public interface RefreshTokenUseCase {
    AuthResultDto refreshTokens(final String authorizationHeader);
}
