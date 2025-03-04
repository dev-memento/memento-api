package com.official.memento.member.infrastructure.persistence.repository;

import com.official.memento.member.infrastructure.persistence.entity.MemberAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.official.memento.auth.domain.AuthProvider;

import java.util.Optional;

public interface MemberAuthEntityJpaRepository extends JpaRepository<MemberAuthEntity, Long> {
    Optional<MemberAuthEntity> findByPlatformIdAndProvider(String platformId, AuthProvider provider);
    Optional<MemberAuthEntity> findByMemberId(Long memberId);
}
