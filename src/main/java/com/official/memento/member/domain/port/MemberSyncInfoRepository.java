package com.official.memento.member.domain.port;

import com.official.memento.member.domain.MemberSyncInfo;

import java.util.Optional;

public interface MemberSyncInfoRepository {

    Optional<MemberSyncInfo> findByMemberId(final long memberId);

    MemberSyncInfo save(MemberSyncInfo memberSyncInfo);

}
