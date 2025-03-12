package com.official.memento.auth.service.dto;

public record MemberInfoDto(
        String accessToken,
        String refreshToken,
        boolean isNewUser,
        boolean isAppleSync,
        boolean isGoogleSync
) {
    public static MemberInfoDto of(final String accessToken, final String refreshToken, final boolean isNewUser, final boolean isAppleSync, boolean isGoogleSync) {
        return new MemberInfoDto(accessToken, refreshToken, isNewUser, isAppleSync, isGoogleSync);
    }
}
