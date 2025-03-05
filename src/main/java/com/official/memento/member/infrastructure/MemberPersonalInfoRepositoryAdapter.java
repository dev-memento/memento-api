package com.official.memento.member.infrastructure;

import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.stereotype.Adapter;
import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.domain.port.MemberPersonalInfoRepository;
import com.official.memento.member.infrastructure.persistence.MemberPersonalInfoMapper;
import com.official.memento.member.infrastructure.persistence.entity.MemberPersonalInfoEntity;
import com.official.memento.member.infrastructure.persistence.repository.MemberPersonalInfoEntityJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class MemberPersonalInfoRepositoryAdapter implements MemberPersonalInfoRepository {

    private final MemberPersonalInfoEntityJpaRepository memberPersonalInfoEntityJpaRepository;

    @Override
    public MemberPersonalInfo findByMemberId(final Long memberId) {
        MemberPersonalInfoEntity entity = memberPersonalInfoEntityJpaRepository
                .findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY));
        return MemberPersonalInfoMapper.toDomain(entity);
    }

    @Override
    public MemberPersonalInfo create(final MemberPersonalInfo memberPersonalInfo) {
        MemberPersonalInfoEntity entityToSave = MemberPersonalInfoMapper.toEntity(memberPersonalInfo);
        MemberPersonalInfoEntity savedEntity = memberPersonalInfoEntityJpaRepository.save(entityToSave);
        return MemberPersonalInfoMapper.toDomain(savedEntity);
    }

    @Override
    public MemberPersonalInfo update(final MemberPersonalInfo memberPersonalInfo) {
        MemberPersonalInfoEntity entity = memberPersonalInfoEntityJpaRepository
                .findByMemberId(memberPersonalInfo.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY));
        entity.updatePersonalInfo(
                memberPersonalInfo.getWakeUpTime(),
                memberPersonalInfo.getWindDownTime(),
                memberPersonalInfo.getJob(),
                memberPersonalInfo.getJobOtherDetail(),
                memberPersonalInfo.getIsStressedUnorganizedSchedule(),
                memberPersonalInfo.getIsForgetImportantThings(),
                memberPersonalInfo.getIsPreferReminder(),
                memberPersonalInfo.getIsImportantBreaks()
        );
        MemberPersonalInfoEntity savedEntity = memberPersonalInfoEntityJpaRepository.save(entity);
        return MemberPersonalInfoMapper.toDomain(savedEntity);
    }


    @Override
    public Optional<MemberPersonalInfo> findNullableByMemberId(Long memberId) {
        return memberPersonalInfoEntityJpaRepository.findByMemberId(memberId)
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