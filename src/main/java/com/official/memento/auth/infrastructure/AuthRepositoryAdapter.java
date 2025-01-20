package com.official.memento.auth.infrastructure;

import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.auth.domain.port.AuthRepository;
import com.official.memento.member.domain.MemberAuth;
import com.official.memento.member.infrastructure.persistence.MemberAuthEntity;
import com.official.memento.member.infrastructure.persistence.MemberAuthJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthRepositoryAdapter implements AuthRepository {

    private final MemberAuthJpaRepository memberAuthJpaRepository;

    public AuthRepositoryAdapter(final MemberAuthJpaRepository memberAuthJpaRepository) {
        this.memberAuthJpaRepository = memberAuthJpaRepository;
    }

    @Override
    public MemberAuth save(final MemberAuth auth) {
        MemberAuthEntity entityToSave = new MemberAuthEntity(
                auth.getId(),
                auth.getProvider(),
                auth.getPlatformId(),
                auth.getRefreshToken(),
                auth.getMemberId()
        );
        MemberAuthEntity savedEntity = memberAuthJpaRepository.save(entityToSave);
        return MemberAuth.withId(
                savedEntity.getId(),
                savedEntity.getMemberId(),
                savedEntity.getProvider(),
                savedEntity.getPlatformId(),
                savedEntity.getRefreshToken()
        );
    }

    @Override
    public Optional<MemberAuth> findByPlatformIdAndProvider(final String platformId, final AuthProvider provider) {
        return memberAuthJpaRepository.findByPlatformIdAndProvider(platformId, provider)
                .map(entity -> MemberAuth.withId(
                        entity.getId(),
                        entity.getMemberId(),
                        entity.getProvider(),
                        entity.getPlatformId(),
                        entity.getRefreshToken()
                ));
    }
}
