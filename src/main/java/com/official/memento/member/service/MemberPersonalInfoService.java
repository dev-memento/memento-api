package com.official.memento.member.service;

import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.domain.port.MemberPersonalInfoRepository;
import com.official.memento.member.service.command.MemberPersonalInfoCommand;
import com.official.memento.member.service.usecase.MemberPersonalInfoRetrieveUseCase;
import com.official.memento.member.service.usecase.MemberPersonalInfoUpdateUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberPersonalInfoService implements MemberPersonalInfoUpdateUseCase, MemberPersonalInfoRetrieveUseCase {

    private final MemberPersonalInfoRepository memberPersonalInfoRepository;

    public MemberPersonalInfoService(final MemberPersonalInfoRepository memberPersonalInfoRepository) {
        this.memberPersonalInfoRepository = memberPersonalInfoRepository;
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
    @Transactional(readOnly = true)
    public MemberPersonalInfo retrieveUptime(final Long memberId) {
        return memberPersonalInfoRepository.findByMemberId(memberId);
    }
}