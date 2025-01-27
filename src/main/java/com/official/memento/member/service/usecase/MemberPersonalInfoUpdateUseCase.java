package com.official.memento.member.service.usecase;

import com.official.memento.member.service.command.MemberPersonalInfoCommand;

@FunctionalInterface
public interface MemberPersonalInfoUpdateUseCase {
    void update(final MemberPersonalInfoCommand command);
}