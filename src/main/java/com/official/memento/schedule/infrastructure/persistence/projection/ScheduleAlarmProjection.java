package com.official.memento.schedule.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record ScheduleAlarmProjection(
        Long scheduleId,
        Long memberId,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer timeZoneOffset
) {
}