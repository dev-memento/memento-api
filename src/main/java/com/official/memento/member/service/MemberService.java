package com.official.memento.member.service;


import com.official.memento.member.domain.Member;
import com.official.memento.member.domain.port.MemberRepository;
import com.official.memento.member.service.usecase.MemberCreateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberCreateUseCase {

    private final MemberRepository memberRepository;

    @Override
    public Member create() {
        return memberRepository.save(Member.createNew());
    }
}
