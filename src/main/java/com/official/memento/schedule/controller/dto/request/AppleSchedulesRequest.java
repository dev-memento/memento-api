package com.official.memento.schedule.controller.dto.request;

import com.official.memento.global.util.Validator;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "애플 일정 생성 요청")
public record AppleSchedulesRequest(
        String syncToken,
        List<AppleScheduleRequest> scheduleCreateRequest
) {
    public AppleSchedulesRequest(
            final String syncToken,
            final List<AppleScheduleRequest> scheduleCreateRequest
    ) {
        Validator.isNull(syncToken);
        this.syncToken = syncToken;
        this.scheduleCreateRequest = scheduleCreateRequest;
    }
}
