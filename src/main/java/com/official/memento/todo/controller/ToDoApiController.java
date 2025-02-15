package com.official.memento.todo.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.global.util.Validator;
import com.official.memento.todo.controller.dto.ToDoAllGetResponse;
import com.official.memento.todo.controller.dto.ToDoCompletionResponse;
import com.official.memento.todo.controller.dto.ToDoCreateRequest;
import com.official.memento.todo.controller.dto.ToDoDetailGetResponse;
import com.official.memento.todo.controller.dto.ToDoDragAndDropRequest;
import com.official.memento.todo.controller.dto.ToDoUpdateRequest;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.service.ToDoCreateUseCase;
import com.official.memento.todo.service.ToDoDeleteUseCase;
import com.official.memento.todo.service.ToDoGetUseCase;
import com.official.memento.todo.service.ToDoUpdateUseCase;
import com.official.memento.todo.service.command.ToDoCompletionUpdateCommand;
import com.official.memento.todo.service.command.ToDoCreateCommand;
import com.official.memento.todo.service.command.ToDoDeleteCommand;
import com.official.memento.todo.service.command.ToDoPositionUpdateCommand;
import com.official.memento.todo.service.command.ToDoUpdateCommand;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class ToDoApiController implements ToDoApiDocs {

    private final ToDoCreateUseCase toDoCreateUseCase;
    private final ToDoDeleteUseCase toDoDeleteUseCase;
    private final ToDoUpdateUseCase toDoUpdateUseCase;
    private final ToDoGetUseCase toDoGetUseCase;

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
    @Override
    public ResponseEntity<SuccessResponse<?>> updateToDo(
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
    @Override
    public ResponseEntity<SuccessResponse<ToDoCompletionResponse>> updateToDoCompletion(
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
                ToDoCompletionResponse.of(toDoId, currentCompletion)
        );
    }

    @GetMapping
    @Override
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

    @PatchMapping("/{toDoId}/position")
    @Override
    public ResponseEntity<SuccessResponse<?>> updateToDoPosition(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId,
            @RequestBody final ToDoDragAndDropRequest request
    ) {
        toDoUpdateUseCase.updatePosition(
                ToDoPositionUpdateCommand.of(authorizationUser.memberId(), toDoId, request.targetOrderNum()));

        return SuccessResponse.of(
                HttpStatus.OK,
                "투두 드래그앤드랍 성공"
        );
    }

    @GetMapping("/date")
    @Override
    public ResponseEntity<SuccessResponse<ToDoAllGetResponse>> getTodoByDate(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestParam final LocalDate date
    ) {
        Validator.isNull(date);
        List<ToDo> todos = toDoGetUseCase.getTodosByDate(1, date);
        return SuccessResponse.of(
                HttpStatus.OK,
                "당일 투두 불러오기 성공",
                ToDoAllGetResponse.of(todos)
        );
    }


    @GetMapping("/{toDoId}")
    @Override
    public ResponseEntity<SuccessResponse<ToDoDetailGetResponse>> getDetailToDos(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId
    ) {
        ToDo toDo = toDoGetUseCase.getDetail(authorizationUser.memberId(), toDoId);
        return SuccessResponse.of(
                HttpStatus.OK,
                "ToDo 디테일 반환 성공",
                ToDoDetailGetResponse.of(toDo)
        );
    }
}
