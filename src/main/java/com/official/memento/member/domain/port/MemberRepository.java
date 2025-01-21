package com.official.memento.member.domain.port;

import com.official.memento.member.domain.Member;
import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.infrastructure.persistence.MemberEntity;
import com.official.memento.member.infrastructure.persistence.MemberPersonalInfoEntity;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member); // MemberEntity 저장
    Optional<Member> findById(Long id); // ID로 MemberEntity 조회
    Optional<MemberPersonalInfo> findPersonalInfoByMemberId(Long memberId);
}
