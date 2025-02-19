package com.official.memento.orderinfo.controller.dto;

import static com.official.memento.global.exception.ErrorCode.INVALID_JSON_FORMAT;

import com.official.memento.global.exception.InvalidRequestBodyException;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ToDo 순서 변경 요청")
public record ToDoUpdateOrderRequest(
        @Schema(description = "옮길 후 앞 순서의 ToDoId", example = "12", nullable = true)
        Long previousToDoId,

        @Schema(description = "옮길 후 뒷 순서의 ToDoId", example = "13", nullable = true)
        Long nextToDoId
) {
    public static ToDoUpdateOrderRequest of(final Long previousToDoId, final Long nextToDoId) {
        return new ToDoUpdateOrderRequest(previousToDoId, nextToDoId);
    }
}
