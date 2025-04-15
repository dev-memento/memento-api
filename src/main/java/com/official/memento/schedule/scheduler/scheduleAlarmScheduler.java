package com.official.memento.schedule.scheduler;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.MementoException;
import com.official.memento.schedule.domain.entity.ScheduleAlarm;
import com.official.memento.schedule.service.CloudTaskAdapter;
import com.official.memento.schedule.service.usecase.ScheduleAlarmGetUseCase;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class scheduleAlarmScheduler {

    private final CloudTaskAdapter cloudTaskAdapter;

    private final ScheduleAlarmGetUseCase scheduleAlarmGetUseCase;

    @Scheduled(cron = "0 15 0 * * *", zone = "UTC") // 매일 UTC 기준 0시 15분
    public void setScheduleAlarm() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(30);
        LocalDateTime tomorrow = now.plusDays(1);
        List<ScheduleAlarm> schedules = scheduleAlarmGetUseCase.getSchedulesBetween(now, tomorrow);
        try{
            for (ScheduleAlarm schedule : schedules) {
                cloudTaskAdapter.createScheduleAlarm(schedule);
            }
        } catch (Exception e) {
            throw new MementoException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
