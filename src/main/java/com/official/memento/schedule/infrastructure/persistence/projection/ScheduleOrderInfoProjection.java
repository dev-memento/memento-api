package com.official.memento.schedule.infrastructure.persistence.projection;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.schedule.domain.enums.ScheduleType;
import com.official.memento.tag.domain.enums.TagColor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ScheduleOrderInfoProjection {
    Long getScheduleId();
    Long getMemberId();
    String getDescription();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    boolean getIsAllDay();
    ScheduleType getType();
    Double getOrderNum();
    String getTagName();
    TagColor getTagColor();
    Long getTagId();
    RepeatOption getRepeatOption();
    LocalDate getRepeatExpiredDate();
    String getScheduleGroupId();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
