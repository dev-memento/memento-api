package com.official.memento.orderinfo.service.usecase;

import java.time.LocalDate;

public interface OrderInfoCreateUseCase {
    void assignToDoOrder(final LocalDate date, final long toDoId, final long memberId);

    void assignScheduleOrder(final LocalDate date, final long scheduleId, final long memberId);
}
