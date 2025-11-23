package com.official.memento.todo.service;

import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.service.command.ToDoCreateCommand;

public interface ToDoCreateUseCase {
    ToDo create(ToDoCreateCommand command);
}
