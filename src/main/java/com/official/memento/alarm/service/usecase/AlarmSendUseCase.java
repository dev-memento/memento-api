package com.official.memento.alarm.service.usecase;

import com.official.memento.alarm.service.command.AlarmExceptionCommand;
import com.official.memento.alarm.service.command.AlarmSendCommand;

public interface AlarmSendUseCase {
    void send(AlarmSendCommand command);
    void sendException(AlarmExceptionCommand command);
}
