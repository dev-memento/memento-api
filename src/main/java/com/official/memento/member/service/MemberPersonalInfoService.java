package com.official.memento.member.service;

import com.official.memento.global.exception.DataBaseIntegrityException;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.domain.port.MemberPersonalInfoRepository;
import com.official.memento.member.service.command.MemberPersonalInfoCommand;
import com.official.memento.member.service.command.MemberPersonalInfoCreateCommand;
import com.official.memento.member.service.command.MemberTimeZoneUpdateCommand;
import com.official.memento.member.service.command.MemberUpTimeUpdateCommand;
import com.official.memento.member.service.usecase.MemberPersonalInfoCreateUseCase;
import com.official.memento.member.service.usecase.MemberPersonalInfoDeleteUseCase;
import com.official.memento.member.service.usecase.MemberPersonalInfoGetUseCase;
import com.official.memento.member.service.usecase.MemberPersonalInfoUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberPersonalInfoService implements
        MemberPersonalInfoCreateUseCase,
        MemberPersonalInfoUpdateUseCase,
        MemberPersonalInfoDeleteUseCase,
        MemberPersonalInfoGetUseCase
{

    private final MemberPersonalInfoRepository memberPersonalInfoRepository;

    @Override
    @Transactional
    public MemberPersonalInfo create(final MemberPersonalInfoCreateCommand command) {
        checkPresentByMemberId(command);
        MemberPersonalInfo memberPersonalInfo = MemberPersonalInfo.of(command.memberId(), command.timeZoneOffset());
        memberPersonalInfoRepository.create(memberPersonalInfo);
        return memberPersonalInfo;
    }

    @Override
    @Transactional
    public void update(final MemberPersonalInfoCommand command) {
        MemberPersonalInfo memberPersonalInfo = memberPersonalInfoRepository.findByMemberId(command.memberId());
        memberPersonalInfo.update(
                command.wakeUpTime(),
                command.windDownTime(),
                command.job(),
                command.jobOtherDetail(),
                command.isStressedUnorganizedSchedule(),
                command.isForgetImportantThings(),
                command.isPreferReminder(),
                command.isImportantBreaks()
        );
        memberPersonalInfoRepository.update(memberPersonalInfo);
    }

    @Override
    @Transactional
    public void updateTimeZone(final MemberTimeZoneUpdateCommand command){
        MemberPersonalInfo memberPersonalInfo = memberPersonalInfoRepository.findByMemberId(command.memberId());
        memberPersonalInfoRepository.update(memberPersonalInfo.updateTimeZoneOffset(command.timeZoneOffset()));
    }

    @Override
    public void updateUpTime(final MemberUpTimeUpdateCommand command){
        MemberPersonalInfo memberPersonalInfo = memberPersonalInfoRepository.findByMemberId(command.memberId());
        memberPersonalInfoRepository.update(memberPersonalInfo.updateUpTime(command.wakeUpTime()));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberPersonalInfo retrieveUptime(final Long memberId) {
        return memberPersonalInfoRepository.findByMemberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberPersonalInfo> findByMemberIdOrNull(long memberId) {
        return memberPersonalInfoRepository.findNullableByMemberId(memberId);
    }

    private void checkPresentByMemberId(final MemberPersonalInfoCreateCommand command) {
        if(memberPersonalInfoRepository.findNullableByMemberId(command.memberId()).isPresent()){
            throw new DataBaseIntegrityException(ErrorCode.DB_INTEGRITY_CONFLICT);
        }
    }

    @Override
    public void deleteByMemberId(final long memberId) {
        memberPersonalInfoRepository.deleteByMemberId(memberId);
    }
}