package com.official.memento.auth.infrastructure;

import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.auth.domain.port.AuthRepository;
import com.official.memento.auth.domain.Auth;
import com.official.memento.auth.infrastructure.persistence.entity.AuthEntity;
import com.official.memento.auth.infrastructure.persistence.repository.AuthEntityJpaRepository;
import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthRepositoryAdapter implements AuthRepository {

    private final AuthEntityJpaRepository authEntityJpaRepository;

    @Override
    public Auth save(final Auth auth) {
        AuthEntity entityToSave = AuthEntity.of(auth);
        AuthEntity savedEntity = authEntityJpaRepository.save(entityToSave);
        return Auth.withId(
                savedEntity.getId(),
                savedEntity.getMemberId(),
                savedEntity.getProvider(),
                savedEntity.getPlatformId(),
                savedEntity.getRefreshToken(),
                savedEntity.getFcmToken()
        );
    }

    @Override
    public Optional<Auth> findByPlatformIdAndProvider(final String platformId, final AuthProvider provider) {
        return authEntityJpaRepository.findByPlatformIdAndProvider(platformId, provider)
                .map(entity -> Auth.withId(
                        entity.getId(),
                        entity.getMemberId(),
                        entity.getProvider(),
                        entity.getPlatformId(),
                        entity.getRefreshToken(),
                        entity.getFcmToken()
                ));
    }

    @Override
    public Optional<Auth> findByMemberIdOrNull(final Long memberId) {
        return authEntityJpaRepository.findByMemberId(memberId)
                .map(entity -> Auth.withId(
                        entity.getId(),
                        entity.getMemberId(),
                        entity.getProvider(),
                        entity.getPlatformId(),
                        entity.getRefreshToken(),
                        entity.getFcmToken()
                ));
    }

    @Override
    public Auth findByMemberId(final Long memberId) {
        return authEntityJpaRepository.findByMemberId(memberId)
                .map(entity -> Auth.withId(
                        entity.getId(),
                        entity.getMemberId(),
                        entity.getProvider(),
                        entity.getPlatformId(),
                        entity.getRefreshToken(),
                        entity.getFcmToken()
                ))
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY));
    }
}
