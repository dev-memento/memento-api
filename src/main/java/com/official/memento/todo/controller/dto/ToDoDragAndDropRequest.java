package com.official.memento.todo.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "ToDo 드래그앤드랍 요청")
public record ToDoDragAndDropRequest(
        @Schema(description = "옮길 위치의 orderNum 순서", example = "10")
        Double targetOrderNum
) {
    public static ToDoDragAndDropRequest of(Double targetOrderNum) {
        return new ToDoDragAndDropRequest(targetOrderNum);
    }
}
