package com.official.memento.schedule.domain;

import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.entity.ScheduleAlarm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Schedule save(final Schedule schedule);

    Schedule update(final Schedule schedule);

    Schedule findById(final long scheduleId);

    Optional<Schedule> findByScheduleGroupIdOrNull(final String scheduleGroupId);

    List<Schedule> findAllByScheduleGroupIdAndStartDateGreaterThanEqual(final String scheduleGroupId, final LocalDateTime startDate);

    void updateTagForSchedules(final long oldTagId, final long newTagId);

    void deleteById(final long scheduleId);

    void deleteAllByMemberId(final long memberId);

    void deleteAllByScheduleGroupId(final String scheduleGroupId);

    List<Schedule> findNonAllDaySchedulesWithOrderInfo(final long memberId);

    List<Schedule> findAllAlDaysByMemberId(final long memberId);

    List<Schedule> findAllByStartDateAndMemberId(final LocalDate startDate, final long memberId);

    List<Schedule> findAllAppleByMemberId(final long memberId);

    List<Schedule> findAllByMemberId(final long memberId);
}
