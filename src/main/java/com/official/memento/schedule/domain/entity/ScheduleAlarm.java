package com.official.memento.schedule.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ScheduleAlarm {

    private final Long scheduleId;
    private final Long memberId;
    private final String description;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final int timeZoneOffset;

    public static ScheduleAlarm of(
            final Long scheduleId,
            final Long memberId,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final int timeZoneOffset
    ) {
        return new ScheduleAlarm(
                scheduleId,
                memberId,
                description,
                startDate,
                endDate,
                timeZoneOffset
        );
    }
}
