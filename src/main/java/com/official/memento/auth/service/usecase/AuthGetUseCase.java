package com.official.memento.auth.service.usecase;

import com.official.memento.auth.domain.Auth;
import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.auth.service.result.AuthResult;

import java.util.Optional;

public interface AuthGetUseCase {
    Optional<Auth> findByPlatformIdAndProvider(final String platformId, final AuthProvider provider);
}
