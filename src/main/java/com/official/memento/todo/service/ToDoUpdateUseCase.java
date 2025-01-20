package com.official.memento.todo.service;

import com.official.memento.todo.service.command.ToDoUpdateCommand;

@FunctionalInterface
public interface ToDoUpdateUseCase {
    void update(final ToDoUpdateCommand toDoUpdateCommand);
}
