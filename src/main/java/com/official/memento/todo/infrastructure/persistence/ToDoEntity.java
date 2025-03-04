package com.official.memento.todo.infrastructure.persistence;

import static lombok.AccessLevel.PROTECTED;

import com.official.memento.global.entity.BaseTimeEntity;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.entity.enums.PriorityType;
import com.official.memento.todo.domain.entity.enums.ToDoType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todo")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ToDoEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long memberId;
    private String groupId;
    private LocalDate startDate;
    private String description;
    private LocalDate endDate;
    private boolean isCompleted;
    @Enumerated(EnumType.STRING)
    private RepeatOption repeatOption;
    private LocalDate repeatExpiredDate;
    private Double priorityUrgency;
    private Double priorityImportance;
    private Double priorityValue;
    @Enumerated(EnumType.STRING)
    private PriorityType priorityType;
    @Enumerated(EnumType.STRING)
    private ToDoType type;
    private long tagId;

    private ToDoEntity(
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

    private ToDoEntity(
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

    public static ToDoEntity of(final ToDo toDo) {
        return new ToDoEntity(
                toDo.getMemberId(),
                toDo.getGroupId(),
                toDo.getStartDate(),
                toDo.getDescription(),
                toDo.getEndDate(),
                toDo.isCompleted(),
                toDo.getRepeatOption(),
                toDo.getRepeatExpiredDate(),
                toDo.getPriorityUrgency(),
                toDo.getPriorityImportance(),
                toDo.getPriorityValue(),
                toDo.getPriorityType(),
                toDo.getType(),
                toDo.getCreatedAt(),
                toDo.getUpdatedAt(),
                toDo.getTagId()
        );
    }

    public static ToDoEntity from(final ToDo toDo) {
        return new ToDoEntity(
                toDo.getId(),
                toDo.getMemberId(),
                toDo.getGroupId(),
                toDo.getStartDate(),
                toDo.getDescription(),
                toDo.getEndDate(),
                toDo.isCompleted(),
                toDo.getRepeatOption(),
                toDo.getRepeatExpiredDate(),
                toDo.getPriorityUrgency(),
                toDo.getPriorityImportance(),
                toDo.getPriorityValue(),
                toDo.getPriorityType(),
                toDo.getType(),
                toDo.getCreatedAt(),
                toDo.getUpdatedAt(),
                toDo.getTagId()
        );
    }

    public void update(
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final long tagId
    ) {
        this.startDate = startDate;
        this.description = description;
        this.endDate = endDate;
        this.priorityUrgency = priorityUrgency;
        this.priorityImportance = priorityImportance;
        this.tagId = tagId;
    }
}
