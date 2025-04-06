package com.official.memento.auth.domain.port;

import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.auth.domain.Auth;
import java.util.Optional;

public interface AuthRepository {
    Auth save(Auth auth);
    Optional<Auth> findByPlatformIdAndProvider(String platformId, AuthProvider provider);
    Optional<Auth> findByMemberIdOrNull(Long memberId);
    Auth findByMemberId(Long memberId);
}
