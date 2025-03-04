package com.official.memento.member.service;

import com.official.memento.global.exception.DataBaseIntegrityException;
import com.official.memento.member.domain.MemberSyncInfo;
import com.official.memento.member.domain.port.MemberSyncInfoRepository;
import com.official.memento.member.service.command.MemberSyncInfoCreateCommand;
import com.official.memento.member.service.usecase.MemberSyncInfoCreateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.official.memento.global.exception.ErrorCode.DB_INTEGRITY_CONFLICT;

@Service
@RequiredArgsConstructor
public class MemberSyncInfoService implements MemberSyncInfoCreateUseCase {

    private final MemberSyncInfoRepository memberSyncInfoRepository;

    @Override
    @Transactional
    public MemberSyncInfo create(final MemberSyncInfoCreateCommand command) {
        checkPresentByMemberId(command);
        MemberSyncInfo memberSyncInfo = MemberSyncInfo.of(command.memberId());
        memberSyncInfoRepository.save(memberSyncInfo);
        return memberSyncInfo;
    }

    private void checkPresentByMemberId(final MemberSyncInfoCreateCommand command) {
        if(memberSyncInfoRepository.findByMemberId(command.memberId()).isPresent()){
            throw new DataBaseIntegrityException(DB_INTEGRITY_CONFLICT);
        }
    }
}
