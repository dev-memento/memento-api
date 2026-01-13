package com.official.memento.todo.controller.dto;

import com.official.memento.todo.service.result.ToDoResult;

import java.util.List;

public record ToDoAllGetResponse(
        List<ToDoGetResponse> toDoGetResponses
) {
    public static ToDoAllGetResponse of(final List<ToDoResult> toDoResults) {
        return new ToDoAllGetResponse(
                toDoResults.stream().map(
                        ToDoGetResponse::of
                ).toList());
    }
}
