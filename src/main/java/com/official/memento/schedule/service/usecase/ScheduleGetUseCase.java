package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.domain.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleGetUseCase {
    List<Schedule> getAllSchedules(final long memberId);

    List<Schedule> getAllAllDaysSchedules(final long memberId);

    Schedule getDetail(final long memberId, final long scheduleId);

    List<Schedule> getSchedules(final long memberId, final LocalDate date);
}
