package com.official.memento.member.service.command;

import com.official.memento.member.service.dto.MemberSyncInfoDto;
import com.official.memento.member.domain.MemberSyncInfo;

public interface MemberSyncInfoGetUseCase {
    MemberSyncInfo findByMemberId(final long memberId);

    MemberSyncInfoDto getMemberSync(final long memberId);
}
