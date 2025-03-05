package com.official.memento.member.domain.port;

import com.official.memento.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member); // MemberEntity 저장

    Optional<Member> findById(Long id); // ID로 MemberEntity 조회
}
