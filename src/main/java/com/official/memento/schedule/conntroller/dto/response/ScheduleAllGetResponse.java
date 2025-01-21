package com.official.memento.schedule.conntroller.dto.response;

import com.official.memento.schedule.domain.entity.Schedule;

import java.util.List;

public record ScheduleAllGetResponse(
    List<ScheduleWithOrderInfo> scheduleWithOrderInfos
) {
    public static ScheduleAllGetResponse of(final List<Schedule> schedules) {
        return new ScheduleAllGetResponse(
                schedules.stream().map(
                        ScheduleWithOrderInfo::of
                ).toList());
    }
}
