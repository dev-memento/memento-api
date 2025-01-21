package com.official.memento.todo.service;

import com.official.memento.todo.domain.ToDo;

import java.util.List;

public interface ToDoGetUseCase {
    List<ToDo> getToDos(long memberId);
}
