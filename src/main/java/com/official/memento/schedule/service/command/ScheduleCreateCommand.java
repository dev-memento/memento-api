package com.official.memento.schedule.service.command;

import com.official.memento.global.exception.InvalidRequestBodyException;

import java.time.LocalDateTime;

import static com.official.memento.global.exception.ErrorCode.NULL_DATA_ERROR;

public record ScheduleCreateCommand(
        long memberId,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isAllDay,
        long tagId
) {
    public static ScheduleCreateCommand of(
            final long memberId,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay,
            final long tagId
    ) {
        return new ScheduleCreateCommand(
                memberId,
                description,
                startDate,
                endDate,
                isAllDay,
                tagId
        );
    }
}
