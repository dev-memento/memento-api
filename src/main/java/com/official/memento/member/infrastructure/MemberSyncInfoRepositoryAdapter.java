package com.official.memento.member.infrastructure;

import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.exception.ErrorCode;
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
    public Optional<MemberSyncInfo> findNullableByMemberId(final long memberId) {
        return memberSyncInfoEntityJpaRepository.findByMemberId(memberId)
                .map(entity -> MemberSyncInfo.withId(
                        entity.getId(),
                        entity.getMemberId(),
                        entity.isAppleSync(),
                        entity.getGoogleSyncToken(),
                        entity.getGoogleRefreshToken()
                ));
    }

    @Override
    public MemberSyncInfo findByMemberId(final long memberId){
        MemberSyncInfoEntity entity = memberSyncInfoEntityJpaRepository.findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY));
        return MemberSyncInfo.withId(
                entity.getId(),
                entity.getMemberId(),
                entity.isAppleSync(),
                entity.getGoogleSyncToken(),
                entity.getGoogleRefreshToken()
        );
    }

    @Override
    public MemberSyncInfo save(final MemberSyncInfo memberSyncInfo) {
        MemberSyncInfoEntity entity = memberSyncInfoEntityJpaRepository.save(MemberSyncInfoEntity.of(memberSyncInfo));
        return MemberSyncInfo.withId(
                entity.getId(),
                entity.getMemberId(),
                entity.isAppleSync(),
                entity.getGoogleSyncToken(),
                entity.getGoogleRefreshToken()
        );
    }

}
