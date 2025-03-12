package com.official.memento.auth.service.dto;

public record MemberInfoDto(
        String accessToken,
        String refreshToken,
        boolean isNewUser
) {
    public static MemberInfoDto of(final String accessToken, final String refreshToken, final boolean isNewUser) {
        return new MemberInfoDto(accessToken, refreshToken, isNewUser);
    }
}
