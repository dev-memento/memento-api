package com.official.memento.member.service;


import com.official.memento.auth.domain.AccessToken;
import com.official.memento.auth.domain.Auth;
import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.auth.domain.RefreshToken;
import com.official.memento.auth.domain.port.AuthClientOutputPort;
import com.official.memento.auth.service.JwtUtil;
import com.official.memento.auth.service.command.AuthCommand;
import com.official.memento.auth.service.result.NewAuthResult;
import com.official.memento.auth.service.usecase.AuthCreateUseCase;
import com.official.memento.auth.service.usecase.AuthDeleteUseCase;
import com.official.memento.auth.service.usecase.AuthGetUseCase;
import com.official.memento.auth.service.usecase.AuthUpdateUseCase;
import com.official.memento.auth.util.AuthValidation;
import com.official.memento.member.domain.Member;
import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.domain.port.MemberRepository;
import com.official.memento.member.service.command.MemberDeleteCommand;
import com.official.memento.member.service.command.MemberPersonalInfoCreateCommand;
import com.official.memento.member.service.command.MemberSyncInfoCreateCommand;
import com.official.memento.member.service.command.MemberTimeZoneUpdateCommand;
import com.official.memento.member.service.usecase.*;
import com.official.memento.schedule.service.usecase.ScheduleDeleteUseCase;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.tag.service.TagCreateUseCase;
import com.official.memento.tag.service.TagDeleteUseCase;
import com.official.memento.tag.service.command.TagCreateCommand;
import com.official.memento.todo.service.ToDoDeleteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberUpdateUseCase, MemberDeleteUseCase {

    private final MemberRepository memberRepository;
    private final AuthCreateUseCase authCreateUseCase;
    private final AuthUpdateUseCase authUpdateUseCase;
    private final AuthGetUseCase authGetUseCase;
    private final ScheduleDeleteUseCase scheduleDeleteUseCase;
    private final ToDoDeleteUseCase toDoDeleteUseCase;
    private final TagCreateUseCase tagCreateUseCase;
    private final TagDeleteUseCase tagDeleteUseCase;
    private final MemberSyncInfoCreateUseCase memberSyncInfoCreateUseCase;
    private final MemberSyncInfoDeleteUseCase memberSyncInfoDeleteUseCase;
    private final MemberPersonalInfoGetUseCase memberPersonalInfoGetUseCase;
    private final MemberPersonalInfoCreateUseCase memberPersonalInfoCreateUseCase;
    private final MemberPersonalInfoUpdateUseCase memberPersonalInfoUpdateUseCase;
    private final MemberPersonalInfoDeleteUseCase memberPersonalInfoDeleteUseCase;
    private final AuthDeleteUseCase authDeleteUseCase;
    private final JwtUtil jwtUtil;
    private final Map<AuthProvider, AuthClientOutputPort> authClientAdapters;

    @Override
    @Transactional
    public NewAuthResult authenticate(final AuthCommand command) {
        final AuthProvider provider = command.providerName();
        final int timeZoneOffset = command.timeZoneOffset();
        final Map<String, Object> tokenInfo = verifyIdToken(provider, command.idToken());
        final String platformId = (String) tokenInfo.get("sub");
        final String email = (String) tokenInfo.get("email");

        Auth auth = authGetUseCase.findByPlatformIdAndProvider(platformId, provider)
                .orElseGet(() -> createNewMember(platformId, provider, timeZoneOffset));

        Optional<MemberPersonalInfo> personalInfo = memberPersonalInfoGetUseCase.findByMemberIdOrNull(auth.getMemberId());
        updateTimeZone(personalInfo, timeZoneOffset);
        boolean isNewUser = isFirstLogin(personalInfo) || isOnboardingIncomplete(personalInfo);

        AccessToken accessToken = jwtUtil.generateAccessToken(auth.getMemberId());
        RefreshToken refreshToken = jwtUtil.generateRefreshToken(auth.getMemberId());

        auth.withUpdatedToken(refreshToken.getToken());
        authUpdateUseCase.update(auth);
        return NewAuthResult.of(accessToken.getToken(), refreshToken.getToken(), isNewUser);
    }

    @Override
    @Transactional
    public void delete(final MemberDeleteCommand memberDeleteCommand) {
        Member member = memberRepository.findById(memberDeleteCommand.memberId());
        scheduleDeleteUseCase.deleteAllByMemberId(member.getId());
        toDoDeleteUseCase.deleteAllByMemberId(member.getId());
        tagDeleteUseCase.deleteAllByMemberId(member.getId());
        memberSyncInfoDeleteUseCase.deleteByMemberId(member.getId());
        memberPersonalInfoDeleteUseCase.deleteByMemberId(member.getId());
        authDeleteUseCase.deleteByMemberId(member.getId());
        memberRepository.delete(member.getId());
    }

    private Map<String, Object> verifyIdToken(final AuthProvider provider, final String idToken) {
        AuthValidation.validateAuthProvider(provider, authClientAdapters);
        final AuthClientOutputPort clientAdapter = authClientAdapters.get(provider);
        return clientAdapter.verifyIdToken(idToken);
    }

    private Auth createNewMember(final String platformId, final AuthProvider provider, final int timeZoneOffset) {
        Member newMember = memberRepository.save(Member.createNew());
        Long memberId = newMember.getId();
        memberPersonalInfoCreateUseCase.create(MemberPersonalInfoCreateCommand.from(memberId, timeZoneOffset));
        memberSyncInfoCreateUseCase.create(MemberSyncInfoCreateCommand.from(memberId));
        createOwnTags(memberId);
        Auth newAuth = Auth.of(memberId, provider, platformId, "");
        return authCreateUseCase.create(memberId,provider,platformId,"");
    }

    private void createOwnTags(Long memberId) {
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.GRAY05, "Untitled"));
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.RED, "Family"));
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.ORANGE, "Hobby"));
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.MINT, "Self-Development"));
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.CYAN, "Work"));
        tagCreateUseCase.create(TagCreateCommand.of(memberId, TagColor.BLUE, "Personal"));
    }

    private void updateTimeZone(final Optional<MemberPersonalInfo> personalInfo, final int timeZoneOffset) {
        if (personalInfo.isPresent() && personalInfo.get().getTimeZoneOffset() != timeZoneOffset) {
            memberPersonalInfoUpdateUseCase.updateTimeZone(MemberTimeZoneUpdateCommand.of(
                    personalInfo.get().getMemberId(),
                    timeZoneOffset
            ));
        }
    }

    private boolean isFirstLogin(Optional<MemberPersonalInfo> personalInfo) {
        return personalInfo.isEmpty();
    }

    private boolean isOnboardingIncomplete(Optional<MemberPersonalInfo> personalInfo) {
        return personalInfo.isPresent() && personalInfo.get().getWakeUpTime() == null;
    }

}