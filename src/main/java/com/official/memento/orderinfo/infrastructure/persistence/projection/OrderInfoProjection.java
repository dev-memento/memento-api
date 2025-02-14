package com.official.memento.orderinfo.infrastructure.persistence.projection;

import com.official.memento.orderinfo.domain.PlanType;

import java.time.LocalDateTime;

public interface OrderInfoProjection {
    long getOrderInfoId();

    long getMemberId();

    Long getScheduleId();

    Long getToDoId();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    Double getPriorityValue();

    double getOrderNum();

    PlanType getPlanType();

    LocalDateTime getCreatedAt();

}
