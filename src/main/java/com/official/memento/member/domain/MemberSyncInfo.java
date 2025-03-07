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

    public MemberSyncInfo(final long memberId, final boolean appleSync, final String googleSyncToken) {
        this.memberId = memberId;
        this.appleSync = appleSync;
        this.googleSyncToken = googleSyncToken;
    }

    public static MemberSyncInfo of(final long memberId) {
        return new MemberSyncInfo(memberId, false, null);
    }

    public static MemberSyncInfo withId(
            final long id,
            final long memberId,
            final boolean appleSyncToken,
            final String googleSyncToken
    ) {
        return new MemberSyncInfo(id, memberId, appleSyncToken, googleSyncToken);
    }

    public MemberSyncInfo updateAppleToken() {
        return new MemberSyncInfo(this.id, this.memberId, !this.appleSync, this.googleSyncToken);
    }

    public MemberSyncInfo updateGoogleToken(final String googleSyncToken) {
        return new MemberSyncInfo(this.id, this.memberId, this.appleSync, googleSyncToken);
    }
}
