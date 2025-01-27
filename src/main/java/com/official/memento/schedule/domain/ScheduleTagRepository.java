package com.official.memento.schedule.domain;

import com.official.memento.schedule.domain.entity.ScheduleTag;

public interface ScheduleTagRepository {
    ScheduleTag save(final ScheduleTag scheduleTag);

    ScheduleTag update(final ScheduleTag scheduleTag);

    ScheduleTag findByScheduleId(final long scheduleId);

    void deleteByScheduleId(final long scheduleId);
}