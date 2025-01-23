package com.official.memento.todo.service;

import com.official.memento.todo.domain.ToDo;

import java.time.LocalDate;
import java.util.List;

public interface ToDoGetUseCase {
    List<ToDo> getToDos(long memberId);

    List<ToDo> getTodosByDate(final long memberId, final LocalDate date);

    ToDo getDetail(final long memberId,final long toDoId);
}
