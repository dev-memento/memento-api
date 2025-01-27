package com.official.memento.auth.service.command;

import com.official.memento.auth.domain.AuthProvider;

public record AuthCommand(
        AuthProvider providerName,
        String idToken
) {
    public static AuthCommand of(final AuthProvider providerName, final String idToken) {
        return new AuthCommand(providerName, idToken);
    }
}
