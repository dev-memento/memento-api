package com.official.memento.member.infrastructure;

import com.official.memento.global.stereotype.Adapter;
import com.official.memento.member.domain.Member;
import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.domain.port.MemberRepository;
import com.official.memento.member.infrastructure.persistence.MemberEntity;
import com.official.memento.member.infrastructure.persistence.MemberJpaRepository;
import com.official.memento.member.infrastructure.persistence.MemberPersonalInfoEntity;
import com.official.memento.member.infrastructure.persistence.MemberPersonalInfoEntityJpaRepository;

import java.util.Optional;

@Adapter
public class MemberRepositoryAdapter implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;
    private final MemberPersonalInfoEntityJpaRepository personalInfoRepository;

    public MemberRepositoryAdapter(MemberJpaRepository memberJpaRepository, MemberPersonalInfoEntityJpaRepository personalInfoRepository) {
        this.memberJpaRepository = memberJpaRepository;
        this.personalInfoRepository = personalInfoRepository;
    }

    @Override
    public Member save(Member member) {
        MemberEntity entityToSave = new MemberEntity();
        entityToSave.setCreatedAt(member.getCreatedAt());
        entityToSave.setUpdatedAt(member.getUpdatedAt());

        MemberEntity savedEntity = memberJpaRepository.save(entityToSave);

        return new Member(
                savedEntity.getId(),
                savedEntity.getCreatedAt(),
                savedEntity.getUpdatedAt()
        );
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id)
                .map(entity -> new Member(
                        entity.getId(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt()
                ));
    }

    @Override
    public Optional<MemberPersonalInfo> findPersonalInfoByMemberId(Long memberId) {
        return personalInfoRepository.findByMemberId(memberId)
                .map(entity -> new MemberPersonalInfo(
                        entity.getId(),
                        entity.getMemberId(),
                        entity.getWakeUpTime(),
                        entity.getWindDownTime(),
                        entity.getJob(),
                        entity.getJobOtherDetail(),
                        entity.getIsStressedUnorganizedSchedule(),
                        entity.getIsForgetImportantThings(),
                        entity.getIsPreferReminder(),
                        entity.getIsImportantBreaks()
                ));
    }
}
