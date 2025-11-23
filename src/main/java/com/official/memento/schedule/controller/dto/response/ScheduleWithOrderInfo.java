package com.official.memento.schedule.controller.dto.response;

import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.enums.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Schema(name = "스케줄 상세 응답")
public record ScheduleWithOrderInfo(
        @Schema(description = "스케줄 아이디", example = "123")
        long id,
        @Schema(description = "일정 제목", example = "데모데이")
        String description,
        @Schema(description = "일정 시작 시간", example = "2025-01-17T11:40:00")
        LocalDateTime startDate,
        @Schema(description = "일정 종료 시간", example = "2025-01-17T11:40:00")
        LocalDateTime endDate,
        @Schema(description = "일정 기간", example = "12PM-4PM (4h)")
        String timeDuration,
        @Schema(description = "AllDay 일정 여부", example = "true")
        boolean isAllDay,
        @Schema(description = "스케줄 생성 타입", example = "NORMAL, GOOGLE, APPLE")
        ScheduleType scheduleType,
        @Schema(description = "순서", example = "2")
        double order,
        @Schema(description = "태그 이름", example = "SOPT")
        String tagName,
        @Schema(description = "태그 색깔", example = "#EE8AAD")
        String tagColorCode
) {
    public static ScheduleWithOrderInfo of(final Schedule schedule) {
        String timeDuration = calculateTimeDuration(schedule.getStartDate(), schedule.getEndDate());
        return new ScheduleWithOrderInfo(
                schedule.getId(),
                schedule.getDescription(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                timeDuration,
                schedule.isAllDay(),
                schedule.getType(),
                schedule.getOrderNum(),
                schedule.getTagName() == null ? "" : schedule.getTagName(),
                schedule.getTagColor() == null ? "" : schedule.getTagColor().getHexCode()
        );
    }

    private static String calculateTimeDuration(LocalDateTime startDate, LocalDateTime endDate) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("ha");
        String startTime = startDate.format(timeFormatter);
        String endTime = endDate.format(timeFormatter);

        return String.format("%s - %s", startTime, endTime);
    }
}
