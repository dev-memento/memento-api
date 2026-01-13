package com.official.memento.todo.service.usecase;

import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.service.command.ToDoPrioritizationCommand;

import java.util.List;

public interface ToDoPrioritizationUseCase {
    List<List<ToDo>> prioritizeWeekly(ToDoPrioritizationCommand command);
    List<ToDo> prioritizeDaily(ToDoPrioritizationCommand command);
}
