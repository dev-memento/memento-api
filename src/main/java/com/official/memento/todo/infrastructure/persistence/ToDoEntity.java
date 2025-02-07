package com.official.memento.todo.infrastructure.persistence;

import static lombok.AccessLevel.PROTECTED;

import com.official.memento.global.entity.BaseTimeEntity;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.todo.domain.ToDo;
import com.official.memento.todo.domain.enums.PriorityType;
import com.official.memento.todo.domain.enums.ToDoType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todo")
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

    private ToDoEntity(
            final long id,
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
            final LocalDateTime updatedAt
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
                toDo.getUpdatedAt()
        );
    }

    public static ToDoEntity withId(final ToDo toDo) {
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
                toDo.getUpdatedAt()
        );
    }

    public void update(
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final Double priorityUrgency,
            final Double priorityImportance
    ) {
        this.startDate = startDate;
        this.description = description;
        this.endDate = endDate;
        this.priorityUrgency = priorityUrgency;
        this.priorityImportance = priorityImportance;

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

    public boolean getIsCompleted() {
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
}
