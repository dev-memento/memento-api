package com.official.memento.orderinfo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name = "ToDo 순서 변경 요청")
public record ToDoUpdateOrderRequest(
        @Schema(description = "순서를 변경할 날짜", example = "2025-01-15", required = true)
        LocalDate date,

        @Schema(description = "옮길 후 앞 순서의 ToDoId", example = "12", nullable = true)
        Long previousToDoId,

        @Schema(description = "옮길 후 뒷 순서의 ToDoId", example = "13", nullable = true)
        Long nextToDoId
) {
    public static ToDoUpdateOrderRequest of(final LocalDate date, final Long previousToDoId, final Long nextToDoId) {
        return new ToDoUpdateOrderRequest(date, previousToDoId, nextToDoId);
    }
}
