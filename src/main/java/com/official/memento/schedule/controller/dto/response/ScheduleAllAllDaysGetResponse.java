package com.official.memento.schedule.controller.dto.response;

import com.official.memento.schedule.domain.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "모든 일정 조회 응답")
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
