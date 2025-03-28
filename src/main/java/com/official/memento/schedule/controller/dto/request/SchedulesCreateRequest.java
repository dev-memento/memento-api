package com.official.memento.schedule.controller.dto.request;

import java.util.List;

public record SchedulesCreateRequest(
        List<ScheduleCreateRequest> createRequests
) {
}
