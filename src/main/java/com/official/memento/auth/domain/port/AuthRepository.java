package com.official.memento.auth.domain.port;

import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.auth.domain.Auth;
import java.util.Optional;

public interface AuthRepository {
    Auth save(Auth auth);
    Optional<Auth> findByPlatformIdAndProvider(final String platformId, AuthProvider provider);
    Optional<Auth> findByMemberIdOrNull(final Long memberId);
    Auth findByMemberId(final Long memberId);
    void deleteByMemberId(final long memberId);
}
