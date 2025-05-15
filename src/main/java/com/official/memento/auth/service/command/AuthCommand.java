package com.official.memento.auth.service.command;

import com.official.memento.auth.domain.AuthProvider;

import java.time.ZoneOffset;

public record AuthCommand(
        AuthProvider providerName,
        String idToken,
        String timeZoneOffset,
        String fcmToken
) {
    public static AuthCommand of(
            final AuthProvider providerName,
            final String idToken,
            final String timeZoneOffset,
            final String fcmToken
    ) {
        return new AuthCommand(providerName, idToken,timeZoneOffset, fcmToken);
    }
}
