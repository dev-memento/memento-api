package com.official.memento.schedule.domain.entity;

import com.official.memento.global.entity.BaseTimeEntity;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.schedule.domain.enums.ScheduleType;
import com.official.memento.tag.domain.enums.TagColor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Schedule extends BaseTimeEntity {
    private Long id;
    private long memberId;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isAllDay;
    private RepeatOption repeatOption;
    private LocalDate repeatExpiredDate;
    private ScheduleType type;
    private String scheduleGroupId;
    private Integer orderNum;
    private Long tagId;
    private String tagName;
    private TagColor tagColor;

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
            final long tagId
    ) {
        this.id = id;
        this.memberId = memberId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAllDay = isAllDay;
        this.repeatOption = repeatOption;
        this.repeatExpiredDate = repeatExpiredDate;
        this.type = type;
        this.scheduleGroupId = scheduleGroupId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tagId = tagId;
    }

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
            final int orderNum,
            final String tagName,
            final TagColor tagColor,
            final long tagId

            ) {
        this.id = id;
        this.memberId = memberId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAllDay = isAllDay;
        this.repeatOption = repeatOption;
        this.repeatExpiredDate = repeatExpiredDate;
        this.type = type;
        this.scheduleGroupId = scheduleGroupId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderNum = orderNum;
        this.tagName = tagName;
        this.tagColor = tagColor;
        this.tagId = tagId;
    }

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
            final String tagName,
            final TagColor tagColor,
            final long tagId
    ) {
        this.id = id;
        this.memberId = memberId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAllDay = isAllDay;
        this.repeatOption = repeatOption;
        this.repeatExpiredDate = repeatExpiredDate;
        this.type = type;
        this.scheduleGroupId = scheduleGroupId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tagName = tagName;
        this.tagColor = tagColor;
        this.tagId = tagId;
    }

    private Schedule(
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
        this.memberId = memberId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAllDay = isAllDay;
        this.repeatOption = repeatOption;
        this.repeatExpiredDate = repeatExpiredDate;
        this.type = type;
        this.scheduleGroupId = scheduleGroupId;
        this.tagId = tagId;
    }

    public static Schedule withId(
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
            final long tagId
    ) {
        return new Schedule(
                id,
                memberId,
                description,
                startDate,
                endDate,
                isAllDay,
                repeatOption,
                repeatExpiredDate,
                type,
                scheduleGroupId,
                createdAt,
                updatedAt,
                tagId
        );
    }

    public static Schedule withIdAndTag(
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
            final String tagName,
            final TagColor tagColor,
            final long tagId
    ) {
        return new Schedule(
                id,
                memberId,
                description,
                startDate,
                endDate,
                isAllDay,
                repeatOption,
                repeatExpiredDate,
                type,
                scheduleGroupId,
                createdAt,
                updatedAt,
                tagName,
                tagColor,
                tagId
        );
    }

    public static Schedule withIdAndOrderAndTag(
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
            final int orderNum,
            final String tagName,
            final TagColor tagColor,
            final long tagId
    ) {
        return new Schedule(
                id,
                memberId,
                description,
                startDate,
                endDate,
                isAllDay,
                repeatOption,
                repeatExpiredDate,
                type,
                scheduleGroupId,
                createdAt,
                updatedAt,
                orderNum,
                tagName,
                tagColor,
                tagId
        );
    }

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
            final Integer timezoneOffset
    ) {
        return new Schedule(
                memberId,
                description,
                startDate.minusHours(timezoneOffset),
                endDate.minusHours(timezoneOffset),
                isAllDay,
                repeatOption,
                repeatExpiredDate,
                type,
                scheduleGroupId,
                tagId
        );
    }

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
        return new Schedule(
                memberId,
                description,
                startDate,
                endDate,
                isAllDay,
                repeatOption,
                repeatExpiredDate,
                type,
                scheduleGroupId,
                tagId
        );
    }

    public Schedule update(
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final boolean isAllDay,
            final long tagId,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final int timezoneOffset
    ) {
        return Schedule.withId(
                id,
                memberId,
                description,
                startDate.minusHours(timezoneOffset),
                endDate.minusHours(timezoneOffset),
                isAllDay,
                repeatOption,
                repeatExpiredDate,
                type,
                scheduleGroupId,
                createdAt,
                LocalDateTime.now(),
                tagId
        );
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public RepeatOption getRepeatOption() {
        return repeatOption;
    }

    public LocalDate getRepeatExpiredDate() {
        return repeatExpiredDate;
    }


    public String getTagName() {
        return tagName;
    }

    public TagColor getTagColor() {
        return tagColor;
    }

    public ScheduleType getType() {
        return type;
    }

    public String getScheduleGroupId() {
        return scheduleGroupId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public Long getTagId() {
        return tagId;
    }
}
