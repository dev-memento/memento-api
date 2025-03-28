package com.official.memento.member.service.usecase;

import com.official.memento.member.service.command.MemberPersonalInfoCommand;

public interface MemberPersonalInfoUpdateUseCase {
    void update(final MemberPersonalInfoCommand command);
    void updateTimeZone(final Long memberId, final int timeZoneOffset);
}