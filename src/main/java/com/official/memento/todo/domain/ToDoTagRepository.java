package com.official.memento.todo.domain;

public interface ToDoTagRepository {
    ToDoTag save(final ToDoTag toDoTag);

    ToDoTag update(final ToDoTag toDoTag);

    ToDoTag findByToDoId(final long toDoId);

    void deleteByToDoId(final long toDoId);
}
