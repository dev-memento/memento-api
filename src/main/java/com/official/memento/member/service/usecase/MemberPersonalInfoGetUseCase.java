package com.official.memento.member.service.usecase;

import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.service.result.MemberPersonalInfoResult;
import java.util.Optional;

public interface MemberPersonalInfoGetUseCase {
    Optional<MemberPersonalInfo> findByMemberIdOrNull(final long memberId);

    MemberPersonalInfoResult findByMemberId(final long memberId);

    MemberPersonalInfo retrieveUptime(final long memberId);
}
