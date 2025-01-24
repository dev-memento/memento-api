package com.official.memento.schedule.conntroller.dto.request;

import java.util.List;

public record AppleSchedulesCreateRequest(
        List<ScheduleCreateRequest> scheduleCreateRequest
) {
}
