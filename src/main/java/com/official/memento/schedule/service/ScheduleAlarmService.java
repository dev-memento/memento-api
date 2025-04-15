package com.official.memento.schedule.service;

import com.official.memento.auth.domain.Auth;
import com.official.memento.auth.domain.port.AuthRepository;
import com.official.memento.schedule.infrastructure.google.fcm.FcmAdapter;
import com.official.memento.schedule.service.command.ScheduleAlarmCreateCommand;
import com.official.memento.schedule.service.usecase.ScheduleAlarmCreateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleAlarmService implements ScheduleAlarmCreateUseCase {

    private final FcmAdapter fcmAdapter;
    private final AuthRepository authRepository;

    @Override
    public void createAlarm(final ScheduleAlarmCreateCommand command) {
        Auth auth = authRepository.findByMemberId(command.memberId());
        fcmAdapter.sendNotification(
                auth.getFcmToken(),
                command.description(),
                command.startTime(),
                command.endTime()
        );
    }
}
