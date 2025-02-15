package com.official.memento.orderinfo.service.usecase;

import com.official.memento.orderinfo.service.command.ToDoPositionUpdateCommand;
import com.official.memento.todo.domain.entity.ToDo;
import java.time.LocalDate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface OrderInfoUpdateUseCase {

    void updatePosition(final ToDoPositionUpdateCommand toDoPositionUpdateCommand);

    @Transactional(propagation = Propagation.REQUIRED)
    void assignOrder(LocalDate date, ToDo toDo, long memberId);
}
