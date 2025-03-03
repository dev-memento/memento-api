package com.official.memento.schedule.conntroller.dto.request;

import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.global.util.Validator;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "단일 일정 생성 요청")
public record ScheduleCreateRequest(
        @Schema(description = "일정 내용", example = "일정디스크립션")
        String description,
        @Schema(description = "일정 시작 날짜", example = "2025-01-20T10:00:00")
        LocalDateTime startDate,
        @Schema(description = "일정 종료 날짜", example = "2025-01-20T12:00:00")
        LocalDateTime endDate,
        @Schema(description = "AllDay 여부", example = "true")
        boolean isAllDay,
        @Schema(description = "태그 아이디", example = "1")
        long tagId
) {
    public ScheduleCreateRequest(
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay,
            final long tagId
    ) {
        checkNullData(description, startDate, endDate);
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAllDay = isAllDay;
        this.tagId = tagId;
    }

    private static void checkNullData(final String description, final LocalDateTime startDate, final LocalDateTime endDate){
        Validator.isNull(description);
        Validator.isNull(startDate);
        Validator.isNull(endDate);
        Validator.validateLengthContainEmoji(description, 30);
    }
}

