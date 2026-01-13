package com.official.memento.todo.service.command;

import java.time.LocalDate;

public record ToDoPrioritizationCommand(
        long memberId,
        LocalDate targetDate
) {
    public static ToDoPrioritizationCommand of(long memberId, LocalDate targetDate) {
        return new ToDoPrioritizationCommand(memberId, targetDate);
    }
}
