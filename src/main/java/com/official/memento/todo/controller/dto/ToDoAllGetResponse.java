package com.official.memento.todo.controller.dto;

import com.official.memento.todo.domain.ToDo;

import java.util.List;

public record ToDoAllGetResponse(
        List<ToDoGetResponse> toDoGetResponses
) {
    public static ToDoAllGetResponse of(final List<ToDo> toDos) {
        return new ToDoAllGetResponse(
                toDos.stream().map(
                        ToDoGetResponse::of
                ).toList());
    }
}
