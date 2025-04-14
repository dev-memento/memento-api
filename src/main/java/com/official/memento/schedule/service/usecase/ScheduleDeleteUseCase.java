package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.command.ScheduleDeleteCommand;

public interface ScheduleDeleteUseCase {
    void delete(final ScheduleDeleteCommand command);
    void deleteAllByMemberId(final long memberId);
}
