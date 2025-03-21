package com.official.memento.member.service.usecase;

public interface MemberSyncInfoUpdateUseCase {
    void activateAppleSync(final long memberId);
    void updateGoogleSyncToken(final long memberId, final String googleSyncToken);
}
