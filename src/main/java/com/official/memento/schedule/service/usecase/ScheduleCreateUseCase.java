package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.command.ScheduleCreateCommand;

import java.util.List;

public interface ScheduleCreateUseCase {
    void create(final ScheduleCreateCommand command);

    void createAppleSchedules(final String syncToken, final List<ScheduleCreateCommand> command);
}
