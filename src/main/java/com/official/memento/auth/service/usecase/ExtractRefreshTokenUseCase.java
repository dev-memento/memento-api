package com.official.memento.auth.service.usecase;

@FunctionalInterface
public interface ExtractRefreshTokenUseCase {
    String extractRefreshToken(String authorizationHeader);
}