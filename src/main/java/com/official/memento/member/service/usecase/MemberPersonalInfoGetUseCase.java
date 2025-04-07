package com.official.memento.member.service.usecase;

import com.official.memento.member.domain.MemberPersonalInfo;

import java.util.Optional;

public interface MemberPersonalInfoGetUseCase {
    Optional<MemberPersonalInfo> findByMemberIdOrNull(final long memberId);

    MemberPersonalInfo retrieveUptime(Long memberId);
}
