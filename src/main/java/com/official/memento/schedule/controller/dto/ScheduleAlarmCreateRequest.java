package com.official.memento.schedule.controller.dto;

import java.time.LocalTime;

public record ScheduleAlarmCreateRequest(
        String description,
        long memberId,
        LocalTime startTime,
        LocalTime endTime
) {
}
