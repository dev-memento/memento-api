package com.official.memento.todo.domain;

import java.time.LocalDate;
import java.util.List;

public interface ToDoRepository {
    ToDo save(final ToDo toDo);

    ToDo update(final ToDo toDo);

    ToDo findById(final long toDoId);

    void deleteById(final long toDoId);

    List<ToDo> findAllByMemberId(final long memberId);

    List<ToDo> findAllByMemberIdAndStartDate(final long memberId, final LocalDate startDate);
}
