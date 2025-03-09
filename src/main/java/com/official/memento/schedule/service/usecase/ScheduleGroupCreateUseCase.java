package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.command.RepeatScheduleCreateCommand;

@FunctionalInterface
public interface ScheduleGroupCreateUseCase {
    void createRepeat(final RepeatScheduleCreateCommand command);
}
