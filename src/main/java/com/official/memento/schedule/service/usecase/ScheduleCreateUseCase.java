package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.command.AppleSchedulesCommand;
import com.official.memento.schedule.service.command.ScheduleCreateCommand;

public interface ScheduleCreateUseCase {

    void create(final ScheduleCreateCommand command);

    void createAppleSchedules(final AppleSchedulesCommand command);

    void syncAppleSchedules(final AppleSchedulesCommand command);

    void syncGoogleSchedules(final long memberId);
}
