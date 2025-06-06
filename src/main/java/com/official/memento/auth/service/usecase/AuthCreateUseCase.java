package com.official.memento.auth.service.usecase;

import com.official.memento.auth.domain.Auth;
import com.official.memento.auth.domain.AuthProvider;

public interface AuthCreateUseCase {
    Auth create(
            final long memberId,
            final AuthProvider provider,
            final String platformId,
            final String refreshToken,
            final String fcmToken
    );
}
