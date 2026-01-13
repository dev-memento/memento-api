package com.official.memento.orderinfo.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderInfo {
    private final Long id;
    private final long memberId;
    private final Long scheduleId;
    private final Long toDoId;
    private final double orderNum;
    private final LocalDate date;
    private final PlanType planType;
    private final LocalDateTime createdAt;

    @Builder(toBuilder = true)
    private OrderInfo(
            final Long id,
            final long memberId,
            final Long scheduleId,
            final Long toDoId,
            final double orderNum,
            final LocalDate date,
            final PlanType planType,
            final LocalDateTime createdAt
    ) {
        this.id = id;
        this.memberId = memberId;
        this.scheduleId = scheduleId;
        this.toDoId = toDoId;
        this.orderNum = orderNum;
        this.date = date;
        this.planType = planType;
        this.createdAt = createdAt;
    }

    // 기존 코드 호환성을 위한 static 팩토리 메서드
    public static OrderInfo of(
            final long memberId,
            final Long scheduleId,
            final Long toDoId,
            final double order,
            final LocalDate date,
            final PlanType planType,
            final LocalDateTime createdAt
    ) {
        return OrderInfo.builder()
                .memberId(memberId)
                .scheduleId(scheduleId)
                .toDoId(toDoId)
                .orderNum(order)
                .date(date)
                .planType(planType)
                .createdAt(createdAt)
                .build();
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
        return OrderInfo.builder()
                .id(id)
                .memberId(memberId)
                .scheduleId(scheduleId)
                .toDoId(toDoId)
                .orderNum(order)
                .date(date)
                .planType(planType)
                .createdAt(createdAt)
                .build();
    }

    // 불변성을 유지하면서 orderNum 증가
    public OrderInfo withIncrementedOrder() {
        return this.toBuilder()
                .orderNum(this.orderNum + 1)
                .build();
    }

    // 불변성을 유지하면서 orderNum 감소
    public OrderInfo withDecrementedOrder() {
        return this.toBuilder()
                .orderNum(this.orderNum - 1)
                .build();
    }

    public OrderInfo updateOrderNum(final double orderNum) {
        return  this.toBuilder()
                .orderNum(this.orderNum + orderNum)
                .build();
    }
}
