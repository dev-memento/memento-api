package com.official.memento.schedule.conntroller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "애플 일정 생성 요청")
public record AppleSchedulesCreateRequest(
        List<ScheduleCreateRequest> scheduleCreateRequest
) {
}
