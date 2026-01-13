package com.official.memento.schedule.domain.entity;

import com.official.memento.global.entity.BaseTimeEntity;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.schedule.domain.enums.ScheduleType;
import com.official.memento.tag.domain.enums.TagColor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
public class Schedule extends BaseTimeEntity {
    private final Long id;
    private final long memberId;
    private final String description;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final boolean isAllDay;
    private final RepeatOption repeatOption;
    private final LocalDate repeatExpiredDate;
    private final ScheduleType type;
    private final String scheduleGroupId;
    private final Long tagId;

    // 비정규화 필드 (DB에 저장되지 않음, 조회 시에만 설정)
    private final Double orderNum;

    private final String tagName;

    private final TagColor tagColor;

    @Builder(toBuilder = true)
    private Schedule(
            final Long id,
            final long memberId,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final ScheduleType type,
            final String scheduleGroupId,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt,
            final Long tagId,
            final Double orderNum,
            final String tagName,
            final TagColor tagColor
    ) {
        this.id = id;
        this.memberId = memberId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAllDay = isAllDay;
        this.repeatOption = repeatOption == null ? RepeatOption.NONE : repeatOption;
        this.repeatExpiredDate = repeatExpiredDate;
        this.type = type;
        this.scheduleGroupId = scheduleGroupId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tagId = tagId;
        this.orderNum = orderNum;
        this.tagName = tagName;
        this.tagColor = tagColor;
    }

    // 도메인 로직: 타임존 계산
    public static Schedule ofCalcTimeZone(
            final long memberId,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final ScheduleType type,
            final String scheduleGroupId,
            final long tagId,
            final String timezoneOffset
    ) {
        return Schedule.builder()
                .memberId(memberId)
                .description(description)
                .startDate(OffsetDateTime.of(startDate, ZoneOffset.of(timezoneOffset)).toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime())
                .endDate(OffsetDateTime.of(endDate, ZoneOffset.of(timezoneOffset)).toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime())
                .isAllDay(isAllDay)
                .repeatOption(repeatOption)
                .repeatExpiredDate(repeatExpiredDate)
                .type(type)
                .scheduleGroupId(scheduleGroupId)
                .tagId(tagId)
                .build();
    }

    // 새 Schedule 생성 (ID 없음)
    public static Schedule of(
            final long memberId,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final ScheduleType type,
            final String scheduleGroupId,
            final long tagId
    ) {
        return Schedule.builder()
                .memberId(memberId)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .isAllDay(isAllDay)
                .repeatOption(repeatOption)
                .repeatExpiredDate(repeatExpiredDate)
                .type(type)
                .scheduleGroupId(scheduleGroupId)
                .tagId(tagId)
                .build();
    }

    public Schedule update(
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay,
            final long tagId,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate
    ) {
        return this.toBuilder()
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .isAllDay(isAllDay)
                .tagId(tagId)
                .repeatOption(repeatOption)
                .repeatExpiredDate(repeatExpiredDate)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
