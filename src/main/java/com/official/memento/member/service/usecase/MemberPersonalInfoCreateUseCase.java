package com.official.memento.member.service.usecase;

import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.service.command.MemberPersonalInfoCreateCommand;

public interface MemberPersonalInfoCreateUseCase {

    MemberPersonalInfo create(MemberPersonalInfoCreateCommand command);
}
