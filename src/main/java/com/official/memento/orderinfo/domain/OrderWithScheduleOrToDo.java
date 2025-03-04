package com.official.memento.orderinfo.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderWithScheduleOrToDo {
    private long orderInfoId;
    private long memberId;
    private Long scheduleId;
    private Long toDoId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double priorityValue;
    private double order;
    private PlanType type;
    private LocalDateTime createdAt;

    public static OrderWithScheduleOrToDo of(
            final Long orderInfoId,
            final long memberId,
            final Long scheduleId,
            final Long toDoId,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final Double priorityValue,
            final double order,
            final PlanType planType,
            final LocalDateTime createdAt

    ) {
        return new OrderWithScheduleOrToDo(
                orderInfoId,
                memberId,
                scheduleId,
                toDoId,
                startDate,
                endDate,
                priorityValue,
                order,
                planType,
                createdAt
        );
    }

    public void shiftBack() {
        this.order++;
    }

    public void shiftForward() {
        this.order--;
    }

    public long getOrderInfoId() {
        return orderInfoId;
    }

    public long getMemberId() {
        return memberId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public Long getToDoId() {
        return toDoId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Double getPriorityValue() {
        return priorityValue;
    }

    public double getOrder() {
        return order;
    }

    public PlanType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
