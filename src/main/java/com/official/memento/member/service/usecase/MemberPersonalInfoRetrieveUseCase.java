package com.official.memento.member.service.usecase;

import com.official.memento.member.domain.MemberPersonalInfo;

@FunctionalInterface
public interface MemberPersonalInfoRetrieveUseCase {
    MemberPersonalInfo retrieveUptime(Long memberId);
}