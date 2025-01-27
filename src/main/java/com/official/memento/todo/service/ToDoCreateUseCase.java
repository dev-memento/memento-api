package com.official.memento.todo.service;

import com.official.memento.todo.service.command.ToDoCreateCommand;

public interface ToDoCreateUseCase {
    void create(ToDoCreateCommand command);
}
