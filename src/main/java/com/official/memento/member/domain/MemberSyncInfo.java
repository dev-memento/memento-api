package com.official.memento.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSyncInfo {
    private Long id;

    private final long memberId;

    private String appleSyncToken;

    private String googleSyncToken;

    public MemberSyncInfo(long memberId, String appleSyncToken, String googleSyncToken) {
        this.memberId = memberId;
        this.appleSyncToken = appleSyncToken;
        this.googleSyncToken = googleSyncToken;
    }

    public static MemberSyncInfo of(final long memberId) {
        return new MemberSyncInfo(memberId, null, null);
    }

    public static MemberSyncInfo withId(final long id, final long memberId, final String appleSyncToken, String googleSyncToken) {
        return new MemberSyncInfo(id, memberId, appleSyncToken, googleSyncToken);
    }

    public MemberSyncInfo updateAppleToken(final String appleSyncToken) {
        return new MemberSyncInfo(this.id, this.memberId, appleSyncToken, this.googleSyncToken);
    }

    public MemberSyncInfo updateGoogleToken(final String googleSyncToken) {
        return new MemberSyncInfo(this.id, this.memberId, this.appleSyncToken, googleSyncToken);
    }
}
