package com.official.memento.schedule.infrastructure.google;

import java.util.List;

public record GoogleCalendarResponse(
        String nextSyncToken,
        List<GoogleCalendarEvent> items
) {
}
