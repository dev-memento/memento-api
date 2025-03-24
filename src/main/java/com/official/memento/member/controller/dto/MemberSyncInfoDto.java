package com.official.memento.member.controller.dto;

public record MemberSyncInfoDto(
        boolean isAppleSync,
        boolean isGoogleSync
) {
    public MemberSyncInfoDto of(final boolean isAppleSync,final boolean isGoogleSync){
        return new MemberSyncInfoDto(isAppleSync, isGoogleSync);
    }
}
