package com.official.memento.schedule.service.command;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.global.exception.InvalidRequestBodyException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.official.memento.global.exception.ErrorCode.NULL_DATA_ERROR;

public record RepeatScheduleCreateCommand(
        long memberId,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isAllDay,
        RepeatOption repeatOption,
        LocalDate repeatExpiredDate,
        Long tagId
) {
    public static RepeatScheduleCreateCommand of(
            final long memberId,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Long tagId
    ) {
        return new RepeatScheduleCreateCommand(
                memberId,
                description,
                startDate,
                endDate,
                isAllDay,
                repeatOption,
                repeatExpiredDate,
                tagId
        );
    }

}