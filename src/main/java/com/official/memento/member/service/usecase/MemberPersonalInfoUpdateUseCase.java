package com.official.memento.member.service.usecase;

import com.official.memento.member.service.command.MemberPersonalInfoCommand;
import com.official.memento.member.service.command.MemberTimeZoneUpdateCommand;
import com.official.memento.member.service.command.MemberUpTimeUpdateCommand;
import com.official.memento.member.service.command.MemberWindDownUpdateCommand;

public interface MemberPersonalInfoUpdateUseCase {
    void update(final MemberPersonalInfoCommand command);
    void updateTimeZone(final MemberTimeZoneUpdateCommand command);
    void updateUpTime(final MemberUpTimeUpdateCommand command);
    void updateWindDown(final MemberWindDownUpdateCommand command);

}