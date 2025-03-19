package com.official.memento.member.service.command;

import com.official.memento.member.service.result.MemberSyncInfoResult;
import com.official.memento.member.domain.MemberSyncInfo;

public interface MemberSyncInfoGetUseCase {
    MemberSyncInfoResult findByMemberId(final long memberId);

    MemberSyncInfoResult getMemberSync(final long memberId);
}
