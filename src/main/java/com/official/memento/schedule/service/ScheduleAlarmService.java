package com.official.memento.schedule.service;

import com.official.memento.schedule.domain.ScheduleAlarmRepository;
import com.official.memento.schedule.domain.ScheduleRepository;
import com.official.memento.schedule.domain.entity.ScheduleAlarm;
import com.official.memento.schedule.service.usecase.ScheduleAlarmGetUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleAlarmService implements ScheduleAlarmGetUseCase {

    private final ScheduleAlarmRepository scheduleAlarmRepository;
    @Override
    public List<ScheduleAlarm> getSchedulesBetween(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        return scheduleAlarmRepository.findSchedulesWithMemberInfoBetween(startDateTime, endDateTime);
    }
}
