package com.official.memento.schedule.infrastructure.google;

import com.official.memento.schedule.domain.entity.Schedule;

public record GoogleCalendarUpdateRequest(
        String description,
        StartEnd start,
        StartEnd end
) {
    public static GoogleCalendarUpdateRequest of(final Schedule schedule) {
        return new GoogleCalendarUpdateRequest(
                schedule.getDescription(),
                new StartEnd(schedule.getStartDate().toString(), schedule.getStartDate().toLocalDate().toString()),
                new StartEnd(schedule.getEndDate().toString(), schedule.getEndDate().toLocalDate().toString())
        );
    }
}
