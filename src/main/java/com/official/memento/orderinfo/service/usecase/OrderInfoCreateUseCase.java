package com.official.memento.orderinfo.service.usecase;

import com.official.memento.todo.domain.entity.ToDo;
import java.time.LocalDate;

public interface OrderInfoCreateUseCase {

    void assignToDoOrder(final LocalDate date, final ToDo toDo, final long memberId);
}
