package com.official.memento.schedule.service.command;

public record SchedulesTagUpdateCommand(
        long currentId,
        long newId
) {
    public static SchedulesTagUpdateCommand of(final long currentId, final long newId) {
        return new SchedulesTagUpdateCommand(currentId, newId);
    }
}
