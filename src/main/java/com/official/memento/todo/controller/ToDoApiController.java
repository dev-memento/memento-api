package com.official.memento.todo.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.schedule.service.command.ScheduleDeleteCommand;
import com.official.memento.todo.controller.dto.ToDoCreateRequest;
import com.official.memento.todo.controller.dto.ToDoUpdateRequest;
import com.official.memento.todo.domain.ToDo;
import com.official.memento.todo.service.ToDoCreateUseCase;
import com.official.memento.todo.service.ToDoDeleteUseCase;
import com.official.memento.todo.service.ToDoUpdateUseCase;
import com.official.memento.todo.service.command.ToDoCreateCommand;
import com.official.memento.todo.service.command.ToDoDeleteCommand;
import com.official.memento.todo.service.command.ToDoUpdateCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/todos")
public class ToDoApiController {

    private final ToDoCreateUseCase toDoCreateUseCase;
    private final ToDoDeleteUseCase toDoDeleteUseCase;
    private final ToDoUpdateUseCase toDoUpdateUseCase;

    public ToDoApiController(
            final ToDoCreateUseCase toDoCreateUseCase,
            final ToDoDeleteUseCase toDoDeleteUseCase,
            final ToDoUpdateUseCase toDoUpdateUseCase
    ) {
        this.toDoCreateUseCase = toDoCreateUseCase;
        this.toDoDeleteUseCase = toDoDeleteUseCase;
        this.toDoUpdateUseCase = toDoUpdateUseCase;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createToDo(
            //@Authorization final AuthorizationUser authorizationUser,
            @RequestBody final ToDoCreateRequest request
    ) {
        //test용, 로그인 구현되면 지우기
        final AuthorizationUser authorizationUser = new AuthorizationUser(2L);

        toDoCreateUseCase.create(ToDoCreateCommand.of(
                        authorizationUser.memberId(),
                        request.date(),
                        request.description(),
                        request.deadline(),
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
    ResponseEntity<SuccessResponse<?>> deleteToDo(
            //@Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId
    ) {
        // todo: 로그인 후 추후 삭제 예정
        toDoDeleteUseCase.delete(ToDoDeleteCommand.of(2L, toDoId));
        return SuccessResponse.of(
                HttpStatus.OK,
                "단일 스케줄 삭제 성공"
        );
    }

    @PatchMapping("/{toDoId}")
    ResponseEntity<SuccessResponse<?>> updateToDo(
            //@Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId,
            @RequestBody final ToDoUpdateRequest request
    ) {
        toDoUpdateUseCase.update(ToDoUpdateCommand.of(
                2,
                toDoId,
                request.date(),
                request.description(),
                request.deadline(),
                request.tagId(),
                request.priorityUrgency(),
                request.priorityImportance()
        ));
        return SuccessResponse.of(
                HttpStatus.OK,
                "단일 스케줄 업데이트 성공"
        );
    }

}
