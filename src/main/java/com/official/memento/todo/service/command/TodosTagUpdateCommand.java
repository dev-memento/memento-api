package com.official.memento.todo.service.command;

public record TodosTagUpdateCommand(
        long currentId,
        long newId
) {
    public static TodosTagUpdateCommand of(final long currentId, final long newId) {
        return new TodosTagUpdateCommand(currentId, newId);
    }
}
