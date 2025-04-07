package com.official.memento.member.domain.port;

import com.official.memento.member.domain.MemberSyncInfo;

import java.util.Optional;

public interface MemberSyncInfoRepository {

    Optional<MemberSyncInfo> findNullableByMemberId(final long memberId);
    MemberSyncInfo findByMemberId(final long memberId);

    MemberSyncInfo save(final MemberSyncInfo memberSyncInfo);

    void deleteById(final long memberId);

}
