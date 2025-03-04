package com.official.memento.auth.util;

import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.auth.domain.port.AuthClientOutputPort;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidIdTokenException;
import com.official.memento.global.exception.UnauthorizedException;

import java.util.Map;

public class AuthValidation {

    public static void validateBearerRefreshToken(final String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public static void validateIdTokenFormat(final String idToken) {
        if (idToken == null || idToken.split("\\.").length != 3) {
            throw new InvalidIdTokenException(ErrorCode.INVALID_ID_TOKEN);
        }
    }

    public static void validateAuthProvider(final AuthProvider provider, final Map<AuthProvider, AuthClientOutputPort> authClientAdapters) {
        if (!authClientAdapters.containsKey(provider)) {
            throw new UnauthorizedException(ErrorCode.UNSUPPORTED_PROVIDER);
        }
    }

}
