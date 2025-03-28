package com.official.memento.schedule.infrastructure.persistence;

import com.official.memento.global.entity.BaseTimeEntity;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.enums.ScheduleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class ScheduleEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long memberId;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isAllDay;
    @Enumerated(EnumType.STRING)
    private RepeatOption repeatOption;
    private LocalDate repeatExpiredDate;
    @Enumerated(EnumType.STRING)
    private ScheduleType type;
    private String scheduleGroupId;
    private long tagId;

    private ScheduleEntity(
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

    private ScheduleEntity(
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

    public static ScheduleEntity of(final Schedule schedule) {
        return new ScheduleEntity(
                schedule.getMemberId(),
                schedule.getDescription(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.isAllDay(),
                schedule.getRepeatOption(),
                schedule.getRepeatExpiredDate(),
                schedule.getType(),
                schedule.getScheduleGroupId(),
                schedule.getTagId()
        );
    }

    public static ScheduleEntity from(final Schedule schedule) {
        return new ScheduleEntity(
                schedule.getId(),
                schedule.getMemberId(),
                schedule.getDescription(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.isAllDay(),
                schedule.getRepeatOption(),
                schedule.getRepeatExpiredDate(),
                schedule.getType(),
                schedule.getScheduleGroupId(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt(),
                schedule.getTagId()
        );
    }
}
