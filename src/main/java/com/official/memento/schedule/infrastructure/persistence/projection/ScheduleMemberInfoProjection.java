package com.official.memento.schedule.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface ScheduleMemberInfoProjection {
    Long getScheduleId();
    Long getMemberId();
    String getDescription();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();

    // MemberPersonalInfoEntity
    LocalTime getWakeUpTime();
    LocalTime getWindDownTime();
    Integer getTimeZoneOffset();
}
