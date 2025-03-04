package com.official.memento.schedule.conntroller.dto.request;

import java.util.List;

public record AppleSchedulesCreateRequest(
        String syncToken,
        List<ScheduleCreateRequest> scheduleCreateRequest
) {
    public AppleSchedulesCreateRequest(final String syncToken, final List<ScheduleCreateRequest> scheduleCreateRequest) {
        this.syncToken = syncToken;
        this.scheduleCreateRequest = scheduleCreateRequest;
    }
}
