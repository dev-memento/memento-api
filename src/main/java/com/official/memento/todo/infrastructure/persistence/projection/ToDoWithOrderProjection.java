package com.official.memento.todo.infrastructure.persistence.projection;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.todo.domain.entity.enums.PriorityType;
import com.official.memento.todo.domain.entity.enums.ToDoType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ToDoWithOrderProjection {
    private final Long id;
    private final Long memberId;
    private final String groupId;
    private final LocalDate startDate;
    private final String description;
    private final LocalDate endDate;
    private final boolean isCompleted;
    private final RepeatOption repeatOption;
    private final LocalDate repeatExpiredDate;
    private final Double priorityUrgency;
    private final Double priorityImportance;
    private final Double priorityValue;
    private final PriorityType priorityType;
    private final ToDoType type;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Integer orderNum;

    public ToDoWithOrderProjection(Long id, Long memberId, String groupId, LocalDate startDate, String description,
                                   LocalDate endDate, boolean isCompleted, RepeatOption repeatOption,
                                   LocalDate repeatExpiredDate, Double priorityUrgency, Double priorityImportance,
                                   Double priorityValue, PriorityType priorityType, ToDoType type,
                                   LocalDateTime createdAt, LocalDateTime updatedAt, Integer orderNum) {
        this.id = id;
        this.memberId = memberId;
        this.groupId = groupId;
        this.startDate = startDate;
        this.description = description;
        this.endDate = endDate;
        this.isCompleted = isCompleted;
        this.repeatOption = repeatOption;
        this.repeatExpiredDate = repeatExpiredDate;
        this.priorityUrgency = priorityUrgency;
        this.priorityImportance = priorityImportance;
        this.priorityValue = priorityValue;
        this.priorityType = priorityType;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderNum = orderNum;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getGroupId() {
        return groupId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public RepeatOption getRepeatOption() {
        return repeatOption;
    }

    public LocalDate getRepeatExpiredDate() {
        return repeatExpiredDate;
    }

    public Double getPriorityUrgency() {
        return priorityUrgency;
    }

    public Double getPriorityImportance() {
        return priorityImportance;
    }

    public Double getPriorityValue() {
        return priorityValue;
    }

    public PriorityType getPriorityType() {
        return priorityType;
    }

    public ToDoType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Integer getOrderNum() {
        return orderNum;
    }
}
