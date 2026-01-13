package com.official.memento.alarm.service;

import com.official.memento.alarm.domain.port.AlarmOutputPort;
import com.official.memento.alarm.service.command.AlarmExceptionCommand;
import com.official.memento.alarm.service.command.AlarmSendCommand;
import com.official.memento.alarm.service.usecase.AlarmSendUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService implements AlarmSendUseCase {

    private final AlarmOutputPort alarmOutputPort;

    // TODO : url parameter 제거, 환경변수로 등록
    @Override
    public void send(AlarmSendCommand command) {
        alarmOutputPort.sendAlarm(command.uri(), command.content());
    }

    @Override
    public void sendException(AlarmExceptionCommand command) {
        alarmOutputPort.sendExceptionAlarm(command.e());
    }
}
