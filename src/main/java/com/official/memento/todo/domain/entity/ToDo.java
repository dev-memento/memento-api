package com.official.memento.todo.domain.entity;

import com.official.memento.global.entity.BaseTimeEntity;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.todo.domain.entity.enums.PriorityType;
import com.official.memento.todo.domain.entity.enums.ToDoType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ToDo extends BaseTimeEntity {
    private Long id;
    private long memberId;
    private String groupId;
    private LocalDate startDate;
    private String description;
    private LocalDate endDate;
    private boolean isCompleted;
    private RepeatOption repeatOption;
    private LocalDate repeatExpiredDate;
    private Double priorityUrgency;
    private Double priorityImportance;
    private Double priorityValue;
    private PriorityType priorityType;
    private ToDoType type;
    private Double orderNum;
    private Long tagId;
    private String tagName;
    private TagColor tagColor;

    private ToDo(
            final Long id,
            final long memberId,
            final String groupId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final boolean isCompleted,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final Double priorityValue,
            final PriorityType priorityType,
            final ToDoType type,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt,
            final long tagId
    ) {
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
        this.tagId = tagId;
    }

    private ToDo(
            final long memberId,
            final String groupId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final boolean isCompleted,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final Double priorityValue,
            final PriorityType priorityType,
            final ToDoType type,
            final long tagId
    ) {
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
        this.tagId = tagId;
    }

    private ToDo(
            final Long id,
            final long memberId,
            final String groupId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final boolean isCompleted,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final Double priorityValue,
            final PriorityType priorityType,
            final ToDoType type,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt,
            final Double orderNum,
            final Long tagId,
            final String tagName,
            final TagColor tagColor
    ) {
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
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagColor = tagColor;
    }

    private ToDo(
            final Long id,
            final long memberId,
            final String groupId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final boolean isCompleted,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final Double priorityValue,
            final PriorityType priorityType,
            final ToDoType type,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt,
            final String tagName,
            final TagColor tagColor
    ) {
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
        this.tagName = tagName;
        this.tagColor = tagColor;
    }

    public static ToDo withId(
            final Long id,
            final long memberId,
            final String groupId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final boolean isCompleted,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final Double priorityValue,
            final PriorityType priorityType,
            final ToDoType type,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt,
            final long tagId
    ) {
        return new ToDo(
                id,
                memberId,
                groupId,
                startDate,
                description,
                endDate,
                isCompleted,
                repeatOption,
                repeatExpiredDate,
                priorityUrgency,
                priorityImportance,
                priorityValue,
                priorityType,
                type,
                createdAt,
                updatedAt,
                tagId
        );
    }

    public static ToDo of(
            final long memberId,
            final String groupId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final boolean isCompleted,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final Double priorityValue,
            final PriorityType priorityType,
            final ToDoType type,
            final long tagId
    ) {
        return new ToDo(
                memberId,
                groupId,
                startDate,
                description,
                endDate,
                isCompleted,
                repeatOption,
                repeatExpiredDate,
                priorityUrgency,
                priorityImportance,
                priorityValue,
                priorityType,
                type,
                tagId
        );
    }

    public static ToDo withIdAndTag(
            final Long id,
            final long memberId,
            final String groupId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final boolean isCompleted,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final Double priorityValue,
            final PriorityType priorityType,
            final ToDoType type,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt,
            final String tagName,
            final TagColor tagColor
    ) {
        return new ToDo(
                id,
                memberId,
                groupId,
                startDate,
                description,
                endDate,
                isCompleted,
                repeatOption,
                repeatExpiredDate,
                priorityUrgency,
                priorityImportance,
                priorityValue,
                priorityType,
                type,
                createdAt,
                updatedAt,
                tagName,
                tagColor
        );
    }

    public static ToDo withIdAndTagAndOrder(
            final Long id,
            final long memberId,
            final String groupId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final boolean isCompleted,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final Double priorityValue,
            final PriorityType priorityType,
            final ToDoType type,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt,
            final Double orderNum,
            final Long tagId,
            final String tagName,
            final TagColor tagColor
    ) {
        return new ToDo(
                id,
                memberId,
                groupId,
                startDate,
                description,
                endDate,
                isCompleted,
                repeatOption,
                repeatExpiredDate,
                priorityUrgency,
                priorityImportance,
                priorityValue,
                priorityType,
                type,
                createdAt,
                updatedAt,
                orderNum,
                tagId,
                tagName,
                tagColor
        );
    }

    public ToDo update(
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final Double orderNum,
            final PriorityType priorityType,
            final Double priorityValue,
            final long tagId
    ) {
        return ToDo.withId(
                id,
                memberId,
                groupId,
                startDate,
                description,
                endDate,
                isCompleted,
                repeatOption,
                repeatExpiredDate,
                priorityUrgency,
                priorityImportance,
                priorityValue,
                priorityType,
                type,
                createdAt,
                LocalDateTime.now(),
                tagId
        );
    }

    public void updateCompletion(
            final boolean isCompleted
    ) {
        this.isCompleted = isCompleted;
    }

    public void updateTagId(Long tagId) {
        this.tagId = tagId;
    }

    public void updateTag(Tag tag) {
        this.tagId = tag.getId();
        this.tagName = tag.getName();
        this.tagColor = tag.getColor();
    }


    public void updateOrderNum(double orderNum) {
        this.orderNum = orderNum;
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
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

    public Double getOrderNum() {
        return orderNum;
    }

    public Long getTagId() {
        return tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public TagColor getTagColor() {
        return tagColor;
    }

    public String toTaskDescription() {
        return "Task: " + this.getDescription() +
                ", id: " + this.getId() +
                ", Urgency : " + this.getPriorityUrgency() +
                ", Importance: " + this.getPriorityImportance() +
                ", Created Date: " + this.getCreatedAt() +
                ", Deadline: " + this.getEndDate();
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
