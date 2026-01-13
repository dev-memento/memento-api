package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.ScheduleResult;
import com.official.memento.schedule.service.command.ScheduleUpdateCommand;
import com.official.memento.schedule.service.command.ScheduleUpdateGroupCommand;
import com.official.memento.schedule.service.command.SchedulesTagUpdateCommand;

public interface ScheduleUpdateUseCase {
    ScheduleResult update(final ScheduleUpdateCommand command);
    void updateGroup(final ScheduleUpdateGroupCommand command);
    void updateGoogle(final ScheduleUpdateCommand command);
    void updateSchedulesTag(final SchedulesTagUpdateCommand command);
}
