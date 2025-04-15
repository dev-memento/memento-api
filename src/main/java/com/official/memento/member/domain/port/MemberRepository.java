package com.official.memento.member.domain.port;

import com.official.memento.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(final Member member); // MemberEntity 저장

    Member findById(final long id); // ID로 MemberEntity 조회

    void delete(final long memberId); // MemberEntity 삭제
}
