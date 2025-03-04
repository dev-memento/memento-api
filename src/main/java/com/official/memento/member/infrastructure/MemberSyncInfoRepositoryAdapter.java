package com.official.memento.member.infrastructure;

import com.official.memento.global.stereotype.Adapter;
import com.official.memento.member.domain.MemberSyncInfo;
import com.official.memento.member.domain.port.MemberSyncInfoRepository;
import com.official.memento.member.infrastructure.persistence.entity.MemberSyncInfoEntity;
import com.official.memento.member.infrastructure.persistence.repository.MemberSyncInfoEntityJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class MemberSyncInfoRepositoryAdapter implements MemberSyncInfoRepository {

    private final MemberSyncInfoEntityJpaRepository memberSyncInfoEntityJpaRepository;

    @Override
    public Optional<MemberSyncInfo> findByMemberId(final long memberId) {
        MemberSyncInfoEntity entity = memberSyncInfoEntityJpaRepository.findByMemberId(memberId);
        return Optional.of(MemberSyncInfo.withId(entity.getId(), entity.getMemberId(), entity.getAppleSyncToken(), entity.getGoogleSyncToken()));
    }

    @Override
    public MemberSyncInfo save(final MemberSyncInfo memberSyncInfo) {
        MemberSyncInfoEntity entity = memberSyncInfoEntityJpaRepository.save(MemberSyncInfoEntity.of(memberSyncInfo));
        return MemberSyncInfo.withId(entity.getId(), entity.getMemberId(), entity.getAppleSyncToken(), entity.getGoogleSyncToken());
    }

}
