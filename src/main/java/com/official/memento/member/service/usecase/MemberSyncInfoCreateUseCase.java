package com.official.memento.member.service.usecase;

import com.official.memento.member.domain.MemberSyncInfo;
import com.official.memento.member.service.command.MemberSyncInfoCreateCommand;

public interface MemberSyncInfoCreateUseCase {

    MemberSyncInfo create(final MemberSyncInfoCreateCommand memberSyncInfoCreateCommand);
}
