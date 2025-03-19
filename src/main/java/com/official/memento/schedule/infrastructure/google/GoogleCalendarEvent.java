package com.official.memento.schedule.infrastructure.google;

import com.official.memento.schedule.domain.entity.ScheduleVo;
import com.official.memento.schedule.domain.enums.ScheduleType;

public record GoogleCalendarEvent(
        String id,
        String status,
        String summary,
        StartEnd start,
        StartEnd end
) {
    public static ScheduleVo toScheduleVo(final GoogleCalendarEvent event) {
        return ScheduleVo.of(
                event.summary() != null ? event.summary() : "", // description
                event.start().toLocalDateTime(),
                event.end().toLocalDateTime(),
                event.id(),                      // scheduleGroupId → Google event id 사용
                event.start().date() != null,    // AllDay 여부 판단 (date 필드 유무로)
                ScheduleType.GOOGLE              // 구글 일정 타입 고정
        );
    }
}
