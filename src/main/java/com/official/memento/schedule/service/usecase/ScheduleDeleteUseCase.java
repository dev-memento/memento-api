package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.command.ScheduleDeleteCommand;
import com.official.memento.schedule.service.command.ScheduleDeleteGroupCommand;

public interface ScheduleDeleteUseCase {
    void delete(final ScheduleDeleteCommand command);
    void deleteGroup(final ScheduleDeleteGroupCommand command);
    void deleteAllByMemberId(final long memberId);
}
