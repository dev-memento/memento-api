package com.official.memento.todo.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.todo.controller.dto.ToDoCreateRequest;
import com.official.memento.todo.service.ToDoCreateUseCase;
import com.official.memento.todo.service.ToDoDeleteUseCase;
import com.official.memento.todo.service.command.ToDoCreateCommand;
import com.official.memento.todo.service.command.ToDoDeleteCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/todos")
public class ToDoApiController {

    private final ToDoCreateUseCase toDoCreateUseCase;
    private final ToDoDeleteUseCase toDoDeleteUseCase;

    public ToDoApiController(
            final ToDoCreateUseCase toDoCreateUseCase,
            final ToDoDeleteUseCase toDoDeleteUseCase
    ) {
        this.toDoCreateUseCase = toDoCreateUseCase;
        this.toDoDeleteUseCase = toDoDeleteUseCase;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createToDo(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final ToDoCreateRequest request
    ) {
        toDoCreateUseCase.create(ToDoCreateCommand.of(
                        authorizationUser.memberId(),
                        request.date(),
                        request.description(),
                        request.deadline(),
                        request.repeatOption(),
                        request.repeatExpiredDate(),
                        request.tagId(),
                        request.priorityUrgency(),
                        request.priorityImportance()
                )
        );
        return SuccessResponse.of(
                HttpStatus.CREATED,
                "ToDo 생성 성공"
        );
    }

    @DeleteMapping("/{toDoId}")
    ResponseEntity<SuccessResponse<?>> deleteToDo(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId
    ) {
        // todo: 로그인 후 추후 삭제 예정
        toDoDeleteUseCase.delete(ToDoDeleteCommand.of(
                authorizationUser.memberId(),
                toDoId)
        );
        return SuccessResponse.of(
                HttpStatus.OK,
                "단일 스케줄 삭제 성공"
        );
    }
}
