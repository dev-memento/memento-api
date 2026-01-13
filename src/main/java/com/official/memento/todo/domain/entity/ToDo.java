package com.official.memento.todo.domain.entity;

import com.official.memento.global.entity.BaseTimeEntity;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.UnauthorizedException;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.todo.domain.entity.enums.PriorityType;
import com.official.memento.todo.domain.entity.enums.ToDoType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ToDo extends BaseTimeEntity {
    private final Long id;
    private final long memberId;
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
    private final Long tagId;

    // 비정규화 필드 (DB에 저장되지 않음, 조회 시에만 설정)
    private final Double orderNum;

    private final String tagName;

    private final TagColor tagColor;

    @Builder(toBuilder = true)
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
            final Long tagId,
            final Double orderNum,
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
        this.repeatOption = repeatOption == null ? RepeatOption.NONE : repeatOption;
        this.repeatExpiredDate = repeatExpiredDate;
        this.priorityUrgency = priorityUrgency;
        this.priorityImportance = priorityImportance;
        // 파생 속성: urgency와 importance로부터 자동 계산
        this.priorityValue = priorityValue != null ? priorityValue : calculatePriorityValue(priorityUrgency, priorityImportance);
        this.priorityType = priorityType != null ? priorityType : determinePriorityType(priorityUrgency, priorityImportance);
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tagId = tagId;
        this.orderNum = orderNum;
        this.tagName = tagName;
        this.tagColor = tagColor;
    }

    // 새 ToDo 생성 (ID 없음)
    // priorityValue와 priorityType은 urgency, importance로부터 자동 계산됨
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
            final ToDoType type,
            final long tagId
    ) {
        return ToDo.builder()
                .memberId(memberId)
                .groupId(groupId)
                .startDate(startDate)
                .description(description)
                .endDate(endDate)
                .isCompleted(isCompleted)
                .repeatOption(repeatOption)
                .repeatExpiredDate(repeatExpiredDate)
                .priorityUrgency(priorityUrgency)
                .priorityImportance(priorityImportance)
                // priorityValue, priorityType은 생성자에서 자동 계산
                .type(type)
                .tagId(tagId)
                .build();
    }

    public ToDo update(
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final Double priorityUrgency,
            final Double priorityImportance,
            final long tagId
    ) {
        return this.toBuilder()
                .startDate(startDate)
                .description(description)
                .endDate(endDate)
                .priorityUrgency(priorityUrgency)
                .priorityImportance(priorityImportance)
                .tagId(tagId)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public String toTaskDescription() {
        return "Task: " + this.description +
                ", id: " + this.id +
                ", Urgency : " + this.priorityUrgency +
                ", Importance: " + this.priorityImportance +
                ", Created Date: " + this.createdAt +
                ", Deadline: " + this.endDate;
    }

    // 비즈니스 로직: 소유권 검증
    public void checkOwn(final long memberId) {
        if (this.memberId != memberId) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    // 비즈니스 로직: Priority 값 계산
    public static Double calculatePriorityValue(Double priorityUrgency, Double priorityImportance) {
        if (priorityUrgency != null && priorityImportance != null) {
            return (priorityUrgency * 0.3) + (priorityImportance * 0.7);
        }
        return -1.0;
    }

    // 비즈니스 로직: Priority 타입 결정
    public static PriorityType determinePriorityType(Double priorityUrgency, Double priorityImportance) {
        return PriorityType.findPriorityType(priorityUrgency, priorityImportance);
    }
}
