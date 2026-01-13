package com.official.memento.schedule.service;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.enums.ScheduleType;
import com.official.memento.tag.domain.enums.TagColor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScheduleResult(
        long scheduleId,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isAllDay,
        long tagId,
        ScheduleType scheduleType,
        String scheduleGroupId,
        RepeatOption repeatOption,
        LocalDate repeatEndDate,
        Double orderNum,
        String tagName,
        TagColor tagColor
) {
    public static ScheduleResult of(final Schedule schedule) {
        return new ScheduleResult(
                schedule.getId(),
                schedule.getDescription(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.isAllDay(),
                schedule.getTagId(),
                schedule.getType(),
                schedule.getScheduleGroupId(),
                schedule.getRepeatOption(),
                schedule.getRepeatExpiredDate(),
                schedule.getOrderNum(),
                schedule.getTagName(),
                schedule.getTagColor()
        );
    }
}
