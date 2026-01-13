package com.official.memento.schedule.service.usecase;

import com.official.memento.schedule.service.ScheduleResult;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleGetUseCase {
    List<ScheduleResult> getAllSchedules(final long memberId);

    List<ScheduleResult> getAllAllDaysSchedules(final long memberId);

    ScheduleResult getDetail(final long memberId, final long scheduleId);

    List<ScheduleResult> getSchedules(final long memberId, final LocalDate date);

}
