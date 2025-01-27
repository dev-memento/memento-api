package com.official.memento.schedule.conntroller.dto.request;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.global.exception.InvalidRequestBodyException;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.official.memento.global.exception.ErrorCode.NULL_DATA_ERROR;

@Schema(name = "스케줄 그룹 업데이트 수정 요청")
public record ScheduleUpdateGroupRequest(
        @Schema(description = "일정 내용", example = "일정디스크립션")
        String description,
        @Schema(description = "일정 시작 날짜", example = "2025-01-20T10:00:00")
        LocalDateTime startDate,
        @Schema(description = "일정 종료 날짜", example = "2025-01-20T12:00:00")
        LocalDateTime endDate,
        @Schema(description = "AllDay 여부", example = "true")
        boolean isAllDay,
        @Schema(description = "반복 옵션", example = "NONE, DAILY, WEEKLY, MONTHLY, YEARLY")
        RepeatOption repeatOption,
        @Schema(description = "반복 종료 날짜", example = "2025-01-20")
        LocalDate repeatExpiredDate,
        @Schema(description = "태그 아이디", example = "1")
        Long tagId,
        @Schema(description = "스케줄 그룹 아이디")
        String scheduleGroupId
) {
    public ScheduleUpdateGroupRequest(
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Long tagId,
            final String scheduleGroupId
    ) {
        checkNullData(description, startDate, endDate, repeatOption, scheduleGroupId);
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAllDay = isAllDay;
        this.repeatOption = repeatOption;
        this.repeatExpiredDate = repeatExpiredDate;
        this.tagId = tagId;
        this.scheduleGroupId = scheduleGroupId;
    }

    private static void checkNullData(
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final RepeatOption repeatOption,
            final String scheduleGroupId
    ) {
        if (description == null | startDate == null || endDate == null || repeatOption == null || scheduleGroupId == null) {
            throw new InvalidRequestBodyException(NULL_DATA_ERROR);
        }
    }
}
