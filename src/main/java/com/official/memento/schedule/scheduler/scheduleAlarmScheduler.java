package com.official.memento.schedule.scheduler;

import com.official.memento.schedule.service.CloudTaskAdapter;
import com.official.memento.schedule.service.usecase.ScheduleGetUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class scheduleAlarmScheduler {

    private final CloudTaskAdapter cloudTaskAdapter;

    private final ScheduleGetUseCase scheduleGetUseCase;

    @Scheduled(cron = "0 30 0 * * *", zone = "UTC") // 매일 UTC 기준 0시 30분
    public void setScheduleAlarm() {

        scheduleGetUseCase.getSchedulesBetween()
    }
}
