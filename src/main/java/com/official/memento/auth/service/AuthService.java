package com.official.memento.auth.service;

import com.official.memento.auth.domain.AccessToken;
import com.official.memento.auth.domain.Auth;
import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.auth.domain.RefreshToken;
import com.official.memento.auth.domain.port.AuthClientOutputPort;
import com.official.memento.auth.domain.port.AuthRepository;
import com.official.memento.auth.service.command.AuthCommand;
import com.official.memento.auth.service.dto.MemberInfoDto;
import com.official.memento.auth.service.usecase.AuthenticateUseCase;
import com.official.memento.auth.service.usecase.RefreshTokenUseCase;
import com.official.memento.auth.util.AuthValidation;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.UnauthorizedException;
import com.official.memento.member.domain.Member;
import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.domain.MemberSyncInfo;
import com.official.memento.member.service.command.MemberPersonalInfoCreateCommand;
import com.official.memento.member.service.command.MemberSyncInfoCreateCommand;
import com.official.memento.member.service.command.MemberSyncInfoGetUseCase;
import com.official.memento.member.service.usecase.MemberCreateUseCase;
import com.official.memento.member.service.usecase.MemberPersonalInfoCreateUseCase;
import com.official.memento.member.service.usecase.MemberPersonalInfoGetUseCase;
import com.official.memento.member.service.usecase.MemberSyncInfoCreateUseCase;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.tag.service.TagCreateUseCase;
import com.official.memento.tag.service.command.TagCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthenticateUseCase, RefreshTokenUseCase {

    private final AuthRepository authRepository;
    private final MemberCreateUseCase memberCreateUseCase;
    private final MemberPersonalInfoCreateUseCase memberPersonalInfoCreateUseCase;
    private final MemberPersonalInfoGetUseCase memberPersonalInfoGetUseCase;
    private final MemberSyncInfoCreateUseCase memberSyncInfoCreateUseCase;
    private final MemberSyncInfoGetUseCase memberSyncInfoGetUseCase;
    private final TagCreateUseCase tagCreateUseCase;
    private final Map<AuthProvider, AuthClientOutputPort> authClientAdapters;
    private final JwtUtil jwtUtil;


    @Override
    @Transactional
    public MemberInfoDto authenticate(final AuthCommand command) {
        final AuthProvider provider = command.providerName();
        final Map<String, Object> tokenInfo = verifyIdToken(provider, command.idToken());
        final String platformId = (String) tokenInfo.get("sub");
        final String email = (String) tokenInfo.get("email");

        Auth auth = authRepository.findByPlatformIdAndProvider(platformId, provider)
                .orElseGet(() -> createNewMember(platformId, provider));

        Optional<MemberPersonalInfo> personalInfo = memberPersonalInfoGetUseCase.findNullableByMemberId(auth.getMemberId());
        MemberSyncInfo memberSyncInfo = memberSyncInfoGetUseCase.findByMemberId(auth.getMemberId());
        boolean isNewUser = isFirstLogin(personalInfo) || isOnboardingIncomplete(personalInfo);

        AccessToken accessToken = jwtUtil.generateAccessToken(auth.getMemberId());
        RefreshToken refreshToken = jwtUtil.generateRefreshToken(auth.getMemberId());

        auth.withUpdatedToken(refreshToken.getToken());
        authRepository.save(auth);
        return MemberInfoDto.of(accessToken.getToken(),refreshToken.getToken(),isNewUser, memberSyncInfo.isAppleSync(), !memberSyncInfo.getGoogleSyncToken().isEmpty());

    }

    @Override
    @Transactional
    public AuthResultDto refreshTokens(final String authorizationHeader) {
        String refreshToken = extractRefreshToken(authorizationHeader);
        if (!jwtUtil.validateToken(extractRefreshToken(refreshToken))) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        String memberId = jwtUtil.getUserIdFromToken(refreshToken);
        Auth auth = authRepository.findByMemberId(Long.parseLong(memberId))
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN));
        if (!auth.getRefreshToken().equals(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        AccessToken newAccessToken = jwtUtil.generateAccessToken(Long.parseLong(memberId));
        RefreshToken newRefreshToken = jwtUtil.generateRefreshToken(Long.parseLong(memberId));
        auth.withUpdatedToken(newRefreshToken.getToken());
        authRepository.save(auth);

        return AuthResultDto.of(newAccessToken.getToken(), newRefreshToken.getToken());
    }

    private String extractRefreshToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private Auth createNewMember(String platformId, AuthProvider provider) {
        Member newMember = memberCreateUseCase.create();
        Long memberId = newMember.getId();
        memberPersonalInfoCreateUseCase.create(MemberPersonalInfoCreateCommand.from(memberId));
        memberSyncInfoCreateUseCase.create(MemberSyncInfoCreateCommand.from(memberId));
        createOwnTags(memberId);
        Auth newAuth = Auth.of(memberId, provider, platformId, "");
        return authRepository.save(newAuth);
    }

    private void createOwnTags(Long memberId) {
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.GRAY05, "Untitled"));
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.RED, "Family"));
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.ORANGE, "Hobby"));
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.MINT, "Self-Development"));
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.CYAN, "Work"));
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.BLUE, "Personal"));
    }

    private Map<String, Object> verifyIdToken(final AuthProvider provider, final String idToken) {
        AuthValidation.validateAuthProvider(provider, authClientAdapters);
        final AuthClientOutputPort clientAdapter = authClientAdapters.get(provider);
        return clientAdapter.verifyIdToken(idToken);
    }

    private boolean isFirstLogin(Optional<MemberPersonalInfo> personalInfo) {
        return personalInfo.isEmpty();
    }

    private boolean isOnboardingIncomplete(Optional<MemberPersonalInfo> personalInfo) {
        return personalInfo.isPresent() && personalInfo.get().getWakeUpTime() == null;
    }
}


