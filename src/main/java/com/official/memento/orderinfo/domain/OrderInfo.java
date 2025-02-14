package com.official.memento.orderinfo.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderInfo {
    private Long id;
    private long memberId;
    private Long scheduleId;
    private Long toDoId;
    private double orderNum;
    private LocalDate date;
    private PlanType planType;
    private LocalDateTime createdAt;

    private OrderInfo(
            final Long memberId,
            final Long scheduleId,
            final Long toDoId,
            final double orderNum,
            final LocalDate date,
            final PlanType planType,
            final LocalDateTime createdAt
    ) {
        this.memberId = memberId;
        this.scheduleId = scheduleId;
        this.toDoId = toDoId;
        this.orderNum = orderNum;
        this.date = date;
        this.planType = planType;
        this.createdAt = createdAt;
    }

    public static OrderInfo of(
            final long memberId,
            final Long scheduleId,
            final Long toDoId,
            final double order,
            final LocalDate date,
            final PlanType planType,
            final LocalDateTime createdAt
    ) {
        return new OrderInfo(memberId,scheduleId, toDoId, order, date, planType, createdAt);
    }

    public static OrderInfo withId(
            final Long id,
            final long memberId,
            final Long scheduleId,
            final Long toDoId,
            final double order,
            final LocalDate date,
            final PlanType planType,
            final LocalDateTime createdAt
    ) {
        return new OrderInfo(id,memberId, scheduleId, toDoId, order, date, planType, createdAt);
    }

    public void updateOrderNum(final double orderNum) {
        this.orderNum = orderNum;
    }

    // 순서를 증가
    public void incrementOrder() {
        this.orderNum += 1;
    }

    // 순서를 감소
    public void decrementOrder() {
        this.orderNum -= 1;

    }

    public void setOrderNum(double orderNum) {
        this.orderNum = orderNum;
    }
}
