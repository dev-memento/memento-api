package com.official.memento.member.infrastructure.persistence.repository;

import com.official.memento.member.infrastructure.persistence.entity.MemberPersonalInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberPersonalInfoEntityJpaRepository extends JpaRepository<MemberPersonalInfoEntity, Long> {
    Optional<MemberPersonalInfoEntity> findByMemberId(final Long memberId);
}
