package com.official.memento.todo.service;

import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.service.command.ToDoCompletionUpdateCommand;
import com.official.memento.todo.service.command.ToDoUpdateCommand;

public interface ToDoUpdateUseCase {
    ToDo update(final ToDoUpdateCommand toDoUpdateCommand);

    boolean updateCompletion(final ToDoCompletionUpdateCommand toDoCompletionUpdateCommand);
}
