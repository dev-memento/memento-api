package com.official.memento.member.service.usecase;

import com.official.memento.member.service.command.MemberDeleteCommand;

public interface MemberDeleteUseCase {

    void delete(final MemberDeleteCommand memberDeleteCommand);
}
