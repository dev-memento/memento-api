package com.official.memento.schedule.infrastructure.google;

import java.time.LocalDateTime;

public record StartEnd(
        String dateTime,
        String date
) {
    public LocalDateTime toLocalDateTime() {
        if (dateTime != null) {
            return LocalDateTime.parse(dateTime.substring(0, 19));
        } else if (date != null) {
            return LocalDateTime.parse(date + "T00:00:00");
        } else {
            throw new IllegalArgumentException("Invalid date or dateTime");
        }
    }
}
