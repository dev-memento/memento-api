package com.official.memento.todo.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.todo.controller.dto.*;
import com.official.memento.todo.domain.ToDo;
import com.official.memento.todo.service.*;
import com.official.memento.todo.service.command.ToDoCompletionUpdateCommand;
import com.official.memento.todo.service.command.ToDoCreateCommand;
import com.official.memento.todo.service.command.ToDoDeleteCommand;
import com.official.memento.todo.service.command.ToDoUpdateCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/v1/todos")
public class ToDoApiController implements ToDoApiDocs {

    private final ToDoCreateUseCase toDoCreateUseCase;
    private final ToDoDeleteUseCase toDoDeleteUseCase;
    private final ToDoUpdateUseCase toDoUpdateUseCase;
    private final ToDoGetUseCase toDoGetUseCase;

    public ToDoApiController(
            final ToDoCreateUseCase toDoCreateUseCase,
            final ToDoDeleteUseCase toDoDeleteUseCase,
            final ToDoUpdateUseCase toDoUpdateUseCase,
            final ToDoGetUseCase toDoGetUseCase
    ) {
        this.toDoCreateUseCase = toDoCreateUseCase;
        this.toDoDeleteUseCase = toDoDeleteUseCase;
        this.toDoUpdateUseCase = toDoUpdateUseCase;
        this.toDoGetUseCase = toDoGetUseCase;
    }

    @PostMapping
    @Override
    public ResponseEntity<SuccessResponse<?>> createToDo(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final ToDoCreateRequest request
    ) {
        toDoCreateUseCase.create(ToDoCreateCommand.of(
                        authorizationUser.memberId(),
                        request.startDate(),
                        request.description(),
                        request.endDate(),
                        RepeatOption.NONE,
                        null,
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
    @Override
    public ResponseEntity<SuccessResponse<?>> deleteToDo(
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

    @PatchMapping("/{toDoId}")
    ResponseEntity<SuccessResponse<?>> updateToDo(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId,
            @RequestBody final ToDoUpdateRequest request
    ) {
        toDoUpdateUseCase.update(ToDoUpdateCommand.of(
                authorizationUser.memberId(),
                toDoId,
                request.startDate(),
                request.description(),
                request.endDate(),
                request.tagId(),
                request.priorityUrgency(),
                request.priorityImportance()
        ));
        return SuccessResponse.of(
                HttpStatus.OK,
                "단일 스케줄 업데이트 성공"
        );
    }

    @PatchMapping("/{toDoId}/completion")
    ResponseEntity<SuccessResponse<ToDoCompletionResponse>> updateToDoCompletion(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId
    ) {

        boolean currentCompletion = toDoUpdateUseCase.updateCompletion(ToDoCompletionUpdateCommand.of(
                authorizationUser.memberId(),
                toDoId
        ));

        return SuccessResponse.of(
                HttpStatus.OK,
                "단일 투두 완료 상태 업데이트 성공",
                ToDoCompletionResponse.of(toDoId,currentCompletion)
        );
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<ToDoAllGetResponse>> getToDos(
            @Authorization final AuthorizationUser authorizationUser
    ) {
        List<ToDo> allToDos = toDoGetUseCase.getToDos(authorizationUser.memberId());
        return SuccessResponse.of(
                HttpStatus.OK,
                "ToDo 조회 목록 성공",
                ToDoAllGetResponse.of(allToDos)
        );
    }
}
