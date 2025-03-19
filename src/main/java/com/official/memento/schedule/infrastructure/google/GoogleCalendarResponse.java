package com.official.memento.schedule.infrastructure.google;

import java.util.List;

public record GoogleCalendarResponse(
        List<GoogleCalendarEvent> items
) {
}
