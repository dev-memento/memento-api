package com.official.memento.auth.service;

import com.official.memento.auth.service.usecase.ExtractRefreshTokenUseCase;
import com.official.memento.auth.service.usecase.RefreshTokenUseCase;
import com.official.memento.auth.util.AuthValidation;
import com.official.memento.global.exception.UnauthorizedException;
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
import com.official.memento.auth.service.usecase.AuthenticateUseCase;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.tag.domain.enums.TagColor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements AuthenticateUseCase, RefreshTokenUseCase, ExtractRefreshTokenUseCase {

    private final Map<AuthProvider, AuthClientOutputPort> authClientAdapters;
    private final AuthRepository authRepository;
    private final MemberRepository memberRepository;
    private final MemberPersonalInfoRepository memberPersonalInfoRepository;
    private final JwtUtil jwtUtil;
    private final TagRepository tagRepository;

    public AuthService(
            Map<AuthProvider, AuthClientOutputPort> authClientAdapters,
            AuthRepository authRepository,
            MemberRepository memberRepository,
            MemberPersonalInfoRepository memberPersonalInfoRepository,
            JwtUtil jwtUtil,
            TagRepository tagRepository
    ) {
        this.authClientAdapters = authClientAdapters;
        this.authRepository = authRepository;
        this.memberRepository = memberRepository;
        this.memberPersonalInfoRepository = memberPersonalInfoRepository;
        this.jwtUtil = jwtUtil;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public AuthResult authenticate(final AuthCommand command) {
        final AuthProvider provider = command.providerName();
        final Map<String, Object> tokenInfo = verifyIdToken(provider, command.idToken());

        final String platformId = (String) tokenInfo.get("sub");
        final String email = (String) tokenInfo.get("email");

        MemberAuth auth = authRepository.findByPlatformIdAndProvider(platformId, provider)
                .orElseGet(() -> createNewMember(platformId, provider));

        Optional<MemberPersonalInfo> personalInfo = memberRepository.findPersonalInfoByMemberId(auth.getMemberId());
        boolean isNewUser = personalInfo.isEmpty() || personalInfo.get().getWakeUpTime() == null;

        AccessToken accessToken = jwtUtil.generateAccessToken(auth.getMemberId());
        RefreshToken refreshToken = jwtUtil.generateRefreshToken(auth.getMemberId());

        auth.withUpdatedToken(refreshToken.getToken());
        authRepository.save(auth);

        return AuthResult.of(accessToken, refreshToken, isNewUser);
    }

    public MemberAuth createNewMember(String platformId, AuthProvider provider) {
        Member newMember = memberRepository.save(Member.createNew());
        Long memberId = newMember.getId();
        MemberAuth newAuth = MemberAuth.of(memberId, provider, platformId, "");
        MemberPersonalInfo personalInfo = MemberPersonalInfo.of(memberId);
        memberPersonalInfoRepository.create(personalInfo);
        tagRepository.save(Tag.of("Untitled", TagColor.GRAY05, memberId));
        tagRepository.save(Tag.of("Family", TagColor.RED, memberId));
        tagRepository.save(Tag.of("Hobby", TagColor.ORANGE, memberId));
        tagRepository.save(Tag.of("Self-Development", TagColor.MINT, memberId));
        tagRepository.save(Tag.of("Work", TagColor.CYAN, memberId));
        tagRepository.save(Tag.of("Personal", TagColor.BLUE, memberId));
        return authRepository.save(newAuth);
    }

    public Map<String, Object> verifyIdToken(final AuthProvider provider, final String idToken) {
        AuthValidation.validateAuthProvider(provider, authClientAdapters);
        final AuthClientOutputPort clientAdapter = authClientAdapters.get(provider);
        return clientAdapter.verifyIdToken(idToken);
    }

    @Override
    @Transactional
    public AuthResult refreshTokens(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        String memberId = jwtUtil.getUserIdFromToken(refreshToken);
        MemberAuth memberAuth = authRepository.findByMemberId(Long.parseLong(memberId))
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN));
        if (!memberAuth.getRefreshToken().equals(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        AccessToken newAccessToken = jwtUtil.generateAccessToken(Long.parseLong(memberId));
        RefreshToken newRefreshToken = jwtUtil.generateRefreshToken(Long.parseLong(memberId));

        memberAuth.withUpdatedToken(newRefreshToken.getToken());
        authRepository.save(memberAuth);

        return AuthResult.of(newAccessToken, newRefreshToken, false);
    }

    @Override
    public String extractRefreshToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }
}


