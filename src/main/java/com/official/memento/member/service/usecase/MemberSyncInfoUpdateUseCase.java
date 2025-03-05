package com.official.memento.member.service.usecase;

public interface MemberSyncInfoUpdateUseCase {
    void updateAppleToken(final long memberId,final String token);
}
