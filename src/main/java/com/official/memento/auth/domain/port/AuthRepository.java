package com.official.memento.auth.domain.port;

import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.member.domain.MemberAuth;
import java.util.Optional;

public interface AuthRepository {
    MemberAuth save(MemberAuth auth);
    Optional<MemberAuth> findByPlatformIdAndProvider(String platformId, AuthProvider provider);
}
