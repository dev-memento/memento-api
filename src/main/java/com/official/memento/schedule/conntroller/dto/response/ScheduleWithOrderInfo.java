package com.official.memento.schedule.conntroller.dto.response;

import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.enums.ScheduleType;

import java.time.LocalDateTime;

public record ScheduleWithOrderInfo(
        long id,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isAllDay,
        ScheduleType scheduleType,
        int order,
        String tagName,
        String tagColorCode
) {
    public static ScheduleWithOrderInfo of(final Schedule schedule) {
        return new ScheduleWithOrderInfo(
                schedule.getId(),
                schedule.getDescription(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.isAllDay(),
                schedule.getType(),
                schedule.getOrderNum(),
                schedule.getTagName(),
                schedule.getTagColor().getHexCode()
        );
    }
}
