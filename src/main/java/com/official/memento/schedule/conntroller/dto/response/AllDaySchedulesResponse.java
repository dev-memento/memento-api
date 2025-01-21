package com.official.memento.schedule.conntroller.dto.response;

import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.enums.ScheduleType;

import java.time.LocalDateTime;

public record AllDaySchedulesResponse(
        long id,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        ScheduleType scheduleType,
        boolean isAllDay,
        String tagName,
        String tagColor
) {
    public static AllDaySchedulesResponse of(final Schedule schedule) {
        return new AllDaySchedulesResponse(
                schedule.getId(),
                schedule.getDescription(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.getType(),
                schedule.isAllDay(),
                schedule.getTagName(),
                schedule.getTagColor().getHexCode()
        );
    }
}