package com.official.memento.schedule.service.command;

import java.time.LocalDateTime;

public record AppleScheduleCreateCommand(
        String scheduleUniqueId,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isAllDay
) {
    public static AppleScheduleCreateCommand of(
            final String scheduleUniqueId,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay
    ) {
        return new AppleScheduleCreateCommand(
                scheduleUniqueId,
                description,
                startDate,
                endDate,
                isAllDay
        );
    }
}
