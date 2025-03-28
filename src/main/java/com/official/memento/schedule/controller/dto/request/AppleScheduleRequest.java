package com.official.memento.schedule.controller.dto.request;

import com.official.memento.global.util.Validator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record AppleScheduleRequest(
        @Schema(description = "고유 ID", example = "3E6F1D20-1A34-11E9-AB14-D663BD873D93")
        String id,
        @Schema(description = "일정 내용", example = "일정디스크립션")
        String description,
        @Schema(description = "일정 시작 날짜", example = "2025-01-20T10:00:00")
        LocalDateTime startDate,
        @Schema(description = "일정 종료 날짜", example = "2025-01-20T12:00:00")
        LocalDateTime endDate,
        @Schema(description = "AllDay 여부", example = "true")
        boolean isAllDay
) {
    public AppleScheduleRequest(
            final String id,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay
    ) {
        checkNullData(id,description, startDate, endDate);
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAllDay = isAllDay;
    }

    private static void checkNullData(
            final String id,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        Validator.isNull(id);
        Validator.isNull(description);
        Validator.isNull(startDate);
        Validator.isNull(endDate);
    }
}
