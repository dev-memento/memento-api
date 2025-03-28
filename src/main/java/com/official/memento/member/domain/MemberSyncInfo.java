package com.official.memento.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSyncInfo {
    private Long id;

    private long memberId;

    private boolean appleSync;

    private String googleSyncToken;
    private String googleRefreshToken;

    public MemberSyncInfo(final long memberId, final boolean appleSync, final String googleSyncToken, final String googleRefreshToken) {
        this.memberId = memberId;
        this.appleSync = appleSync;
        this.googleSyncToken = googleSyncToken;
        this.googleRefreshToken = googleRefreshToken;
    }

    public static MemberSyncInfo of(final long memberId) {
        return new MemberSyncInfo(memberId, false, null,null);
    }

    public static MemberSyncInfo withId(
            final long id,
            final long memberId,
            final boolean appleSyncToken,
            final String googleSyncToken,
            final String googleRefreshToken
    ) {
        return new MemberSyncInfo(id, memberId, appleSyncToken, googleSyncToken, googleRefreshToken);
    }

    public MemberSyncInfo activateAppleToken() {
        return new MemberSyncInfo(this.id, this.memberId, true, this.googleSyncToken, this.googleRefreshToken);
    }

    public MemberSyncInfo updateGoogleToken(final String googleSyncToken) {
        return new MemberSyncInfo(this.id, this.memberId, this.appleSync, googleSyncToken, this.googleRefreshToken);
    }
}
