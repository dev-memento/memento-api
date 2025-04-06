package com.official.memento.member.infrastructure.persistence.repository;

import com.official.memento.member.infrastructure.persistence.entity.MemberPersonalInfoEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberPersonalInfoEntityJpaRepository extends JpaRepository<MemberPersonalInfoEntity, Long> {
    Optional<MemberPersonalInfoEntity> findByMemberId(final Long memberId);

    @NotNull List<MemberPersonalInfoEntity> findAll();
}
