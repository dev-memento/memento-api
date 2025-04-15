package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.command.ScheduleAlarmCreateCommand;


public interface ScheduleAlarmCreateUseCase {

    void createAlarm(final ScheduleAlarmCreateCommand command);
}
