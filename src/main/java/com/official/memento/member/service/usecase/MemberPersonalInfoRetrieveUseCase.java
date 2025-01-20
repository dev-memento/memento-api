package com.official.memento.member.service.usecase;

import com.official.memento.member.controller.dto.MemberUptimeResponse;
import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.service.command.MemberPersonalInfoCommand;

@FunctionalInterface
public interface MemberPersonalInfoRetrieveUseCase {
    MemberPersonalInfo retrieveUptime(Long memberId);
}