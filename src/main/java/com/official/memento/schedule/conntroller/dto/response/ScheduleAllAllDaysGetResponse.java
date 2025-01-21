package com.official.memento.schedule.conntroller.dto.response;

import com.official.memento.schedule.domain.entity.Schedule;

import java.util.List;

public record ScheduleAllAllDaysGetResponse(
        List<AllDaySchedulesResponse> allDaySchedulesList
) {
    public static ScheduleAllAllDaysGetResponse of(final List<Schedule> schedules){
        return new ScheduleAllAllDaysGetResponse(
                schedules.stream().map(
                        AllDaySchedulesResponse::of
                ).toList()
        );
    }
}
