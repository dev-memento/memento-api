package com.official.memento.todo.domain.repository;

import com.official.memento.todo.domain.entity.ToDo;
import java.time.LocalDate;
import java.util.List;

public interface ToDoRepository {
    ToDo save(final ToDo toDo);

    ToDo update(final ToDo toDo);

    ToDo findById(final long toDoId);

    void deleteById(final long toDoId);

    void deleteAllByMemberId(final long memberId);

    List<ToDo> findAllByMemberId(final long memberId);

    List<ToDo> findAllByMemberIdAndStartDate(final long memberId, final LocalDate startDate);

    List<ToDo> findByMemberIdAndEndDateAndIsCompleted(final long memberId, final LocalDate endDate, final boolean isCompleted);
}
