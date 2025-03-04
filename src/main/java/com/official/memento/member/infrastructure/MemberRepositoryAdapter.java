package com.official.memento.member.infrastructure;

import com.official.memento.global.stereotype.Adapter;
import com.official.memento.member.domain.Member;
import com.official.memento.member.domain.port.MemberRepository;
import com.official.memento.member.infrastructure.persistence.entity.MemberEntity;
import com.official.memento.member.infrastructure.persistence.repository.MemberEntityJpaRepository;
import com.official.memento.member.infrastructure.persistence.repository.MemberPersonalInfoEntityJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {

    private final MemberEntityJpaRepository memberEntityJpaRepository;
    private final MemberPersonalInfoEntityJpaRepository personalInfoRepository;

    @Override
    public Member save(Member member) {
        MemberEntity entityToSave = new MemberEntity();
        entityToSave.setCreatedAt(member.getCreatedAt());
        entityToSave.setUpdatedAt(member.getUpdatedAt());

        MemberEntity savedEntity = memberEntityJpaRepository.save(entityToSave);

        return new Member(
                savedEntity.getId(),
                savedEntity.getCreatedAt(),
                savedEntity.getUpdatedAt()
        );
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberEntityJpaRepository.findById(id)
                .map(entity -> new Member(
                        entity.getId(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt()
                ));
    }
}
