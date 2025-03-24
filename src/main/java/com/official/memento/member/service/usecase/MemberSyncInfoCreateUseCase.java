package com.official.memento.member.service.usecase;

import com.official.memento.member.domain.MemberSyncInfo;
import com.official.memento.member.service.command.MemberSyncInfoCreateCommand;
import com.official.memento.member.service.result.MemberSyncInfoResult;

public interface MemberSyncInfoCreateUseCase {

    MemberSyncInfoResult create(final MemberSyncInfoCreateCommand memberSyncInfoCreateCommand);
}
