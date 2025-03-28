package com.official.memento.auth.service.command;

import com.official.memento.auth.domain.AuthProvider;

public record AuthCommand(
        AuthProvider providerName,
        String idToken,
        int timeZoneOffset
) {
    public static AuthCommand of(final AuthProvider providerName, final String idToken,final int timeZoneOffset) {
        return new AuthCommand(providerName, idToken,timeZoneOffset);
    }
}
