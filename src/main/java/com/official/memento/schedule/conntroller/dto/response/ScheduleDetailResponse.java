package com.official.memento.schedule.conntroller.dto.response;

import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.enums.ScheduleType;

import java.time.LocalDateTime;

public record ScheduleDetailResponse(
        long id,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isAllDay,
        ScheduleType scheduleType,
        long tagId
        //태그 아이디, 스케줄 타입
) {
   public static ScheduleDetailResponse of(final Schedule schedule){
      return new ScheduleDetailResponse(
              schedule.getId(),
              schedule.getDescription(),
              schedule.getStartDate(),
              schedule.getEndDate(),
              schedule.isAllDay(),
              schedule.getType(),
              schedule.getTagId()
      );
   }
}
