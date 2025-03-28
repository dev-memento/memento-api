package com.official.memento.member.service.usecase;

import com.official.memento.member.service.command.MemberPersonalInfoCommand;
import com.official.memento.member.service.command.MemberTimeZoneUpdateCommand;

public interface MemberPersonalInfoUpdateUseCase {
    void update(final MemberPersonalInfoCommand command);
    void updateTimeZone(final MemberTimeZoneUpdateCommand command);
}