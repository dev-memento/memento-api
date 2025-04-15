package com.official.memento.schedule.infrastructure;

import com.official.memento.global.stereotype.Adapter;
import com.official.memento.schedule.domain.ScheduleAlarmRepository;
import com.official.memento.schedule.domain.entity.ScheduleAlarm;
import com.official.memento.schedule.infrastructure.persistence.ScheduleAlarmCustomRepository;
import com.official.memento.schedule.infrastructure.persistence.projection.ScheduleAlarmProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class ScheduleAlarmRepositoryAdapter implements ScheduleAlarmRepository {

    private final ScheduleAlarmCustomRepository scheduleAlarmCustomRepository;

    @Override
    public List<ScheduleAlarm> findSchedulesWithMemberInfoBetween(final LocalDateTime startTime,
                                                                  final LocalDateTime endTime) {
        List<ScheduleAlarmProjection> scheduleAlarmProjections = scheduleAlarmCustomRepository.findSchedulesWithMemberInfoBetween(
                startTime, endTime);
        return scheduleAlarmProjections.stream().map(scheduleEntity -> ScheduleAlarm.of(
                scheduleEntity.scheduleId(),
                scheduleEntity.memberId(),
                scheduleEntity.description(),
                scheduleEntity.startDate(),
                scheduleEntity.endDate(),
                scheduleEntity.timeZoneOffset()
        )).toList();
    }
}
