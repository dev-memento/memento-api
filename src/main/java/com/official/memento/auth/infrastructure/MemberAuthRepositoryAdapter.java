package com.official.memento.auth.infrastructure;

import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.auth.domain.port.MemberAuthRepository;
import com.official.memento.member.domain.MemberAuth;
import com.official.memento.member.infrastructure.persistence.entity.MemberAuthEntity;
import com.official.memento.member.infrastructure.persistence.repository.MemberAuthEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberAuthRepositoryAdapter implements MemberAuthRepository {

    private final MemberAuthEntityJpaRepository memberAuthEntityJpaRepository;

    @Override
    public MemberAuth save(final MemberAuth auth) {
        MemberAuthEntity entityToSave = new MemberAuthEntity(
                auth.getId(),
                auth.getProvider(),
                auth.getPlatformId(),
                auth.getRefreshToken(),
                auth.getMemberId()
        );
        MemberAuthEntity savedEntity = memberAuthEntityJpaRepository.save(entityToSave);
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
        return memberAuthEntityJpaRepository.findByPlatformIdAndProvider(platformId, provider)
                .map(entity -> MemberAuth.withId(
                        entity.getId(),
                        entity.getMemberId(),
                        entity.getProvider(),
                        entity.getPlatformId(),
                        entity.getRefreshToken()
                ));
    }

    @Override
    public Optional<MemberAuth> findByMemberId(final Long memberId) {
        return memberAuthEntityJpaRepository.findByMemberId(memberId)
                .map(entity -> MemberAuth.withId(
                        entity.getId(),
                        entity.getMemberId(),
                        entity.getProvider(),
                        entity.getPlatformId(),
                        entity.getRefreshToken()
                ));
    }
}
