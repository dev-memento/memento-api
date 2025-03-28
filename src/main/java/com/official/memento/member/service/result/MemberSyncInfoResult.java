package com.official.memento.member.service.result;

import com.official.memento.member.domain.MemberSyncInfo;

public record MemberSyncInfoResult(
        boolean isAppleSync,
        String googleSyncToken,
        String googleRefreshToken
) {
    public static MemberSyncInfoResult of(final MemberSyncInfo memberSyncInfo) {
        return new MemberSyncInfoResult(memberSyncInfo.isAppleSync(), memberSyncInfo.getGoogleSyncToken(), memberSyncInfo.getGoogleRefreshToken());
    }
}
