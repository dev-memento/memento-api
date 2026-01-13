package com.official.memento.todo.domain.vo;

import java.time.LocalDate;

public record PrioritizedToDo(
        String task,
        Long id,
        LocalDate createdDate,
        LocalDate deadline,
        Float priority,
        Float urgency,
        Float importance,
        Double order
) {
}
