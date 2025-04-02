package com.official.memento.schedule.scheduler;

import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.service.CloudTaskAdapter;
import com.official.memento.schedule.service.usecase.ScheduleGetUseCase;
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

    private final ScheduleGetUseCase scheduleGetUseCase;

    @Scheduled(cron = "0 30 0 * * *", zone = "UTC") // 매일 UTC 기준 0시 30분
    public void setScheduleAlarm() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime tomorrow = now.plusDays(1);
        List<Schedule> schedules = scheduleGetUseCase.getSchedulesBetween(now, tomorrow);
    }
}
