package com.official.memento.schedule.domain;

import com.official.memento.schedule.domain.entity.ScheduleAlarm;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleAlarmRepository {
    List<ScheduleAlarm> findSchedulesWithMemberInfoBetween(final LocalDateTime startTime, final LocalDateTime endTime);
}
