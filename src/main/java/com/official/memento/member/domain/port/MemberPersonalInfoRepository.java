package com.official.memento.member.domain.port;

import com.official.memento.member.domain.MemberPersonalInfo;

import java.util.List;
import java.util.Optional;

public interface MemberPersonalInfoRepository {

    MemberPersonalInfo findByMemberId(final Long memberId);

    MemberPersonalInfo create(final MemberPersonalInfo memberPersonalInfo);

    MemberPersonalInfo update(final MemberPersonalInfo memberPersonalInfo);

    Optional<MemberPersonalInfo> findNullableByMemberId(final Long memberId);

    List<MemberPersonalInfo> findAll();

    void deleteByMemberId(final long memberId);
}