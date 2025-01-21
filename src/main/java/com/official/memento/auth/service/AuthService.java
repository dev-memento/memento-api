package com.official.memento.auth.service;

import com.official.memento.member.domain.Member;
import com.official.memento.member.domain.MemberAuth;
import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.domain.port.MemberPersonalInfoRepository;
import com.official.memento.member.domain.port.MemberRepository;
import com.official.memento.auth.domain.AccessToken;
import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.auth.domain.RefreshToken;
import com.official.memento.auth.domain.port.AuthClientOutputPort;
import com.official.memento.auth.domain.port.AuthRepository;
import com.official.memento.auth.service.command.AuthCommand;
import com.official.memento.auth.service.usecase.AuthUseCase;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.MementoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuthService implements AuthUseCase {

    private final Map<AuthProvider, AuthClientOutputPort> authClientAdapters;
    private final AuthRepository authRepository;
    private final MemberRepository memberRepository;
    private final MemberPersonalInfoRepository memberPersonalInfoRepository;
    private final JwtUtil jwtUtil;

    public AuthService(
            Map<AuthProvider, AuthClientOutputPort> authClientAdapters,
            AuthRepository authRepository,
            MemberRepository memberRepository,
            MemberPersonalInfoRepository memberPersonalInfoRepository,
            JwtUtil jwtUtil
    ) {
        this.authClientAdapters = authClientAdapters;
        this.authRepository = authRepository;
        this.memberRepository = memberRepository;
        this.memberPersonalInfoRepository = memberPersonalInfoRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public AuthResult authenticate(final AuthCommand command) {
        final AuthProvider provider = getAuthProvider(command.providerName());
        final Map<String, Object> tokenInfo = verifyIdToken(provider, command.idToken());

        final String platformId = (String) tokenInfo.get("sub");
        final String email = (String) tokenInfo.get("email");

        MemberAuth auth = authRepository.findByPlatformIdAndProvider(platformId, provider)
                .orElseGet(() -> createNewMember(platformId, provider));

        boolean isNewUser = memberRepository.findPersonalInfoByMemberId(auth.getMemberId()).isEmpty();

        AccessToken accessToken = jwtUtil.generateAccessToken(String.valueOf(auth.getMemberId()));
        RefreshToken refreshToken = jwtUtil.generateRefreshToken(String.valueOf(auth.getMemberId()));

        auth.withUpdatedToken(refreshToken.getToken());
        authRepository.save(auth);

        Member member = new Member(
                auth.getId(),
                null,
                null
        );

        return new AuthResult(accessToken, refreshToken, member, isNewUser);
    }

    @Transactional
    private MemberAuth createNewMember(String platformId, AuthProvider provider) {
        Member newMember = memberRepository.save(Member.createNew());
        MemberAuth newAuth = MemberAuth.of(newMember.getId(), provider, platformId, "");
        MemberPersonalInfo personalInfo = MemberPersonalInfo.of(newMember.getId());
        memberPersonalInfoRepository.create(personalInfo);
        return authRepository.save(newAuth);
    }

    @Transactional
    private AuthProvider getAuthProvider(final String providerName) {
        try {
            return AuthProvider.valueOf(providerName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MementoException(ErrorCode.UNSUPPORTED_PROVIDER);
        }
    }

    @Transactional
    private Map<String, Object> verifyIdToken(final AuthProvider provider, final String idToken) {
        final AuthClientOutputPort clientAdapter = authClientAdapters.get(provider);
        if (clientAdapter == null) {
            throw new MementoException(ErrorCode.UNSUPPORTED_PROVIDER);
        }
        try {
            return clientAdapter.verifyIdToken(idToken);
        } catch (Exception e) {
            throw new MementoException(ErrorCode.INVALID_ID_TOKEN);
        }
    }

    @Transactional
    public AuthResult refreshTokens(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new MementoException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        String memberId = jwtUtil.getUserIdFromToken(refreshToken);
        MemberAuth memberAuth = authRepository.findByMemberId(Long.parseLong(memberId))
                .orElseThrow(() -> new MementoException(ErrorCode.INVALID_REFRESH_TOKEN));
        if (!memberAuth.getRefreshToken().equals(refreshToken)) {
            throw new MementoException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        AccessToken newAccessToken = jwtUtil.generateAccessToken(memberId);
        RefreshToken newRefreshToken = jwtUtil.generateRefreshToken(memberId);

        memberAuth.withUpdatedToken(newRefreshToken.getToken());
        authRepository.save(memberAuth);

        return new AuthResult(newAccessToken, newRefreshToken, null, false);

    }
}


