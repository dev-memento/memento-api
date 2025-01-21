package com.official.memento.todo.service;

import com.official.memento.todo.domain.ToDo;

public interface ToDoGetUseCase {
    ToDo getToDoById(Long id);
}
