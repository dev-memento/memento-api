package com.official.memento.orderinfo.service.usecase;

public interface OrderInfoDeleteUseCase {
    void deleteByToDoId(final long todoId);

    void deleteByScheduleId(final long scheduleId);
}
