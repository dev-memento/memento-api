package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.command.ScheduleDeleteGroupCommand;

@FunctionalInterface
public interface ScheduleDeleteGroupUseCase {
    void deleteGroup(final ScheduleDeleteGroupCommand command);
}
