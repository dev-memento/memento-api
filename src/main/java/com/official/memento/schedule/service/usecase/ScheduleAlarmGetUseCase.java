package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.entity.ScheduleAlarm;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleAlarmGetUseCase {
    List<ScheduleAlarm> getSchedulesBetween(final LocalDateTime startDateTime, final LocalDateTime endDateTime);
}
