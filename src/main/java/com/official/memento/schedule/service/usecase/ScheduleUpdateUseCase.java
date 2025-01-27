package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.command.ScheduleUpdateCommand;

@FunctionalInterface
public interface ScheduleUpdateUseCase {
    void update(final ScheduleUpdateCommand command);
}
