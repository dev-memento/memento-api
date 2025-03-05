package com.official.memento.auth.infrastructure.persistence.repository;

import com.official.memento.auth.infrastructure.persistence.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.official.memento.auth.domain.AuthProvider;

import java.util.Optional;

public interface AuthEntityJpaRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByPlatformIdAndProvider(String platformId, AuthProvider provider);
    Optional<AuthEntity> findByMemberId(Long memberId);
}
