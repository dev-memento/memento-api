package com.official.memento.todo.service.usecase;

import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.service.command.ToDoCompletionUpdateCommand;
import com.official.memento.todo.service.command.ToDoUpdateCommand;
import com.official.memento.todo.service.command.TodosTagUpdateCommand;

public interface ToDoUpdateUseCase {
    ToDo update(final ToDoUpdateCommand toDoUpdateCommand);

    boolean updateCompletion(final ToDoCompletionUpdateCommand toDoCompletionUpdateCommand);

    void updateTodosTag(final TodosTagUpdateCommand todosTagUpdateCommand);
}
