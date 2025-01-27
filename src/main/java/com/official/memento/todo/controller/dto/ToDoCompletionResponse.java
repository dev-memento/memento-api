package com.official.memento.todo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "투두 완료 상태 변경 응답")
public record ToDoCompletionResponse(
        @Schema(description = "투두 ID", example = "1")
        long id,
        @Schema(description = "완료 상태")
        boolean isCompleted
) {
    public static ToDoCompletionResponse of(long id, boolean isCompleted) {
        return new ToDoCompletionResponse(id, isCompleted);
    }
}
