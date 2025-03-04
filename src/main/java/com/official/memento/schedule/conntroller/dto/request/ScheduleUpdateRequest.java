package com.official.memento.schedule.conntroller.dto.request;

import com.official.memento.global.util.Validator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "일정 수정 요청")
public record ScheduleUpdateRequest(
        @Schema(description = "일정 내용", example = "수정된 일정")
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
    public ScheduleUpdateRequest of(
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay,
            final long tagId
    ) {
        checkNullData(description, startDate, endDate);
        return new ScheduleUpdateRequest(description, startDate, endDate, isAllDay, tagId);
    }

    private static void checkNullData(final String description, final LocalDateTime startDate, final LocalDateTime endDate){
        Validator.isNull(description);
        Validator.isNull(startDate);
        Validator.isNull(endDate);
        Validator.validateLengthContainEmoji(description, 30);
    }
}
