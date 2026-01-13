package com.official.memento.todo.controller.dto.response;

import com.official.memento.todo.domain.entity.ToDo;

import java.util.List;
import java.util.stream.Collectors;

public record PrioritizationResponse(
        List<List<ToDoPrioritizedGetResponse>> todos
) {
    public static PrioritizationResponse of(List<List<ToDo>> todos) {
        return new PrioritizationResponse(
                todos.stream()
                        .map(toDos -> toDos.stream()
                                .map(toDo -> new ToDoPrioritizedGetResponse(
                                        toDo.getId(),
                                        toDo.getGroupId(),
                                        toDo.getDescription(),
                                        toDo.getStartDate().toString(),
                                        toDo.getEndDate().toString(),
                                        toDo.isCompleted(),
                                        toDo.getPriorityValue(),
                                        toDo.getPriorityType().name(),
                                        toDo.getTagName(),
                                        toDo.getTagColor().getHexCode(),
                                        toDo.getType(),
                                        toDo.getOrderNum()
                                ))
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList())
        );
    }
}
