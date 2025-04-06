package com.official.memento.schedule.service.command;

import java.time.LocalTime;

public record ScheduleAlarmCreateCommand(
        String description,
        long memberId,
        LocalTime startTime,
        LocalTime endTime
) {
    public static ScheduleAlarmCreateCommand of(
            final String description,
            final long memberId,
            final LocalTime startTime,
            final LocalTime endTime
    ) {
        return new ScheduleAlarmCreateCommand(description, memberId, startTime, endTime);
    }
}
