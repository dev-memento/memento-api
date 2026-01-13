package com.official.memento.schedule.service;

import com.official.memento.schedule.domain.ScheduleRepository;
import com.official.memento.schedule.service.usecase.ScheduleGetUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleQueryService implements ScheduleGetUseCase {

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResult> getAllSchedules(final long memberId) {
        return scheduleRepository.findNonAllDaySchedulesWithOrderInfo(memberId).stream()
                .map(ScheduleResult::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResult> getAllAllDaysSchedules(final long memberId) {
        return scheduleRepository.findAllAlDaysByMemberId(memberId).stream()
                .map(ScheduleResult::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResult getDetail(final long memberId, final long scheduleId) {
        var schedule = scheduleRepository.findById(scheduleId);
        if (schedule.getMemberId() != memberId) {
            throw new IllegalArgumentException("해당 스케줄을 소유하지 않음");
        }
        return ScheduleResult.of(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResult> getSchedules(final long memberId, final LocalDate date) {
        return scheduleRepository.findAllByStartDateAndMemberId(date, memberId).stream()
                .map(ScheduleResult::of)
                .toList();
    }
}
