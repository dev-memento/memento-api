package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.ScheduleResult;
import com.official.memento.schedule.service.command.ScheduleUpdateCommand;

public interface ScheduleUpdateUseCase {
    ScheduleResult update(final ScheduleUpdateCommand command);
    void updateGoogle(final ScheduleUpdateCommand command);
}
