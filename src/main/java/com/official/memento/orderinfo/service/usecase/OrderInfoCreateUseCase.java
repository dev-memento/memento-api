package com.official.memento.orderinfo.service.usecase;

import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.todo.domain.entity.ToDo;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface OrderInfoCreateUseCase {
    void assignToDoOrder(final LocalDate date, final ToDo toDo, final long memberId);

   void assignScheduleOrder(final LocalDate date, final Schedule schedule, final long memberId);
}
