package com.official.memento.auth.service.command;

public record AuthCommand(
        String providerName,
        String idToken
) {
    public static AuthCommand of(final String providerName, final String idToken) {
        return new AuthCommand(providerName, idToken);
    }
}
