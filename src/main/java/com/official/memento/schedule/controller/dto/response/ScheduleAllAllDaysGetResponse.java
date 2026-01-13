package com.official.memento.schedule.controller.dto.response;

import com.official.memento.schedule.service.ScheduleResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "모든 일정 조회 응답")
public record ScheduleAllAllDaysGetResponse(
        List<AllDaySchedulesResponse> allDaySchedulesList
) {
    public static ScheduleAllAllDaysGetResponse of(final List<ScheduleResult> scheduleResults){
        return new ScheduleAllAllDaysGetResponse(
                scheduleResults.stream().map(
                        AllDaySchedulesResponse::of
                ).toList()
        );
    }
}
