package com.official.memento.schedule.controller.dto.response;

import com.official.memento.schedule.service.ScheduleResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "모든 일정 조회 응답")
public record ScheduleAllGetResponse(
    List<ScheduleWithOrderInfo> scheduleWithOrderInfos
) {
    public static ScheduleAllGetResponse of(final List<ScheduleResult> scheduleResults) {
        return new ScheduleAllGetResponse(
                scheduleResults.stream().map(
                        ScheduleWithOrderInfo::of
                ).toList());
    }
}
