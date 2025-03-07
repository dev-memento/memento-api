package com.official.memento.schedule.domain.entity;

import com.official.memento.schedule.domain.enums.ScheduleType;

import java.time.LocalDateTime;

public record ScheduleVo(

        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String scheduleGroupId,
        ScheduleType scheduleType
) {
    public static ScheduleVo of(
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final String scheduleGroupId,
            final ScheduleType scheduleType
    ) {
        return new ScheduleVo(
                description,
                startDate,
                endDate,
                scheduleGroupId,
                scheduleType
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleVo that = (ScheduleVo) o;
        return description.equals(that.description) &&
                startDate.equals(that.startDate) &&
                endDate.equals(that.endDate) &&
                scheduleGroupId.equals(that.scheduleGroupId) &&
                scheduleType == that.scheduleType;
    }
}
