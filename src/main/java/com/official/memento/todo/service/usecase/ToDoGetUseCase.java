package com.official.memento.todo.service.usecase;

import com.official.memento.todo.service.result.ToDoResult;

import java.time.LocalDate;
import java.util.List;

public interface ToDoGetUseCase {
    List<ToDoResult> getToDos(long memberId);

    List<ToDoResult> getTodosByDate(final long memberId, final LocalDate date);

    ToDoResult getDetail(final long memberId,final long toDoId);
}
