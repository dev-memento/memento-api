package com.official.memento.todo.controller.dto

import com.official.memento.todo.domain.entity.ToDo
import com.official.memento.todo.domain.entity.enums.ToDoType
import io.swagger.v3.oas.annotations.media.Schema

data class PrioritizationResponse(
    val todos: List<List<ToDoPrioritizedGetResponse>>,
) {
    companion object {
        @JvmStatic
        fun of(todos: List<List<ToDo>>): PrioritizationResponse {
            return PrioritizationResponse(
                todos =
                    todos.map { toDos ->
                        toDos.map { toDo ->
                            ToDoPrioritizedGetResponse(
                                id = toDo.id!!,
                                groupId = toDo.groupId,
                                description = toDo.description,
                                startDate = toDo.startDate.toString(),
                                endDate = toDo.endDate.toString(),
                                isCompleted = toDo.isCompleted,
                                priorityValue = toDo.priorityValue,
                                priorityType = toDo.priorityType.name,
                                tagName = toDo.tagName,
                                tagColor = toDo.tagColor.hexCode,
                                toDoType = toDo.type,
                                orderNum = toDo.orderNum,
                            )
                        }
                    },
            )
        }
    }
}

data class PrioritizationDailyResponse(
    val todos: List<ToDoPrioritizedGetResponse>
) {
    companion object {
        fun of(todos: List<ToDo>): PrioritizationDailyResponse {
            return PrioritizationDailyResponse(
                todos = todos.map { toDo ->
                    ToDoPrioritizedGetResponse(
                        id = toDo.id!!,
                        groupId = toDo.groupId,
                        description = toDo.description,
                        startDate = toDo.startDate.toString(),
                        endDate = toDo.endDate.toString(),
                        isCompleted = toDo.isCompleted,
                        priorityValue = toDo.priorityValue,
                        priorityType = toDo.priorityType.name,
                        tagName = toDo.tagName,
                        tagColor = toDo.tagColor.hexCode,
                        toDoType = toDo.type,
                        orderNum = toDo.orderNum,
                    )
                }
            )
        }

    }

}

@Schema(name = "ToDo 목록 응답")
data class ToDoPrioritizedGetResponse(
        @Schema(description = "ToDo ID")
    val id: Long,
        @Schema(description = "그룹 ID")
    val groupId: String,
        @Schema(description = "설명")
    val description: String,
        @Schema(description = "시작일")
    val startDate: String,
        @Schema(description = "마감일")
    val endDate: String,
        @Schema(description = "완료 여부")
    val isCompleted: Boolean,
        @Schema(description = "우선순위 값")
    val priorityValue: Double,
        @Schema(description = "우선순위 타입")
    val priorityType: String,
        @Schema(description = "태그 이름")
    val tagName: String,
        @Schema(description = "태그 색상")
    val tagColor: String,
        @Schema(description = "ToDo 유형")
    val toDoType: ToDoType,
        @Schema(description = "정렬 순서")
    val orderNum: Double,
)
