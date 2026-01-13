package com.official.memento.orderinfo.service.usecase;

import com.official.memento.orderinfo.domain.OrderInfo;

import java.time.LocalDate;

public interface OrderInfoGetUseCase {
    OrderInfo findByToDoId(final long toDoId);

    OrderInfo findByScheduleId(final long scheduleId);

    OrderInfo findByToDoIdAndDate(Long toDoId, LocalDate date);
}
