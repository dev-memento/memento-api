package com.official.memento.todo.domain;

import com.official.memento.global.entity.BaseTimeEntity;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.todo.domain.enums.PriorityType;
import com.official.memento.todo.domain.enums.ToDoType;
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
            final LocalDateTime updatedAt
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
            final ToDoType type
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
            final LocalDateTime updatedAt
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
                updatedAt
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
            final ToDoType type
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
                type
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

    public void update(
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final Double orderNum,
            final PriorityType priorityType,
            final Double priorityValue
    ) {
        this.startDate = startDate;
        this.description = description;
        this.endDate = endDate;
        this.priorityUrgency = priorityUrgency;
        this.priorityImportance = priorityImportance;
        this.orderNum = orderNum;
        this.priorityType = priorityType;
        this.priorityValue = priorityValue;
    }

    public void updateCompletion(
            final boolean isCompleted
    ) {
        this.isCompleted = isCompleted;
    }

    public void updateTagId(Long tagId) {
        this.tagId = tagId;
    }

    public void updateTagColor(TagColor tagColor) {
        this.tagColor = tagColor;
    }

    public void updateTag(Tag tag) {
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

    public double getOrderNum() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setRepeatOption(RepeatOption repeatOption) {
        this.repeatOption = repeatOption;
    }

    public void setRepeatExpiredDate(LocalDate repeatExpiredDate) {
        this.repeatExpiredDate = repeatExpiredDate;
    }

    public void setPriorityUrgency(Double priorityUrgency) {
        this.priorityUrgency = priorityUrgency;
    }

    public void setPriorityImportance(Double priorityImportance) {
        this.priorityImportance = priorityImportance;
    }

    public void setPriorityValue(Double priorityValue) {
        this.priorityValue = priorityValue;
    }

    public void setPriorityType(PriorityType priorityType) {
        this.priorityType = priorityType;
    }

    public void setType(ToDoType type) {
        this.type = type;
    }

    public String toTaskDescription() {
        return "Task: " + this.getDescription() +
                ", id: " + this.getId() +
                ", Urgency : " + this.getPriorityUrgency() +
                ", Importance: " + this.getPriorityImportance() +
                ", Created Date: " + this.getCreatedAt() +
                ", Deadline: " + this.getEndDate();
    }
}
