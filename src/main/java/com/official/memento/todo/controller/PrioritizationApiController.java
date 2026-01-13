package com.official.memento.todo.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.todo.controller.dto.request.PrioritizationRequest;
import com.official.memento.todo.controller.dto.response.PrioritizationDailyResponse;
import com.official.memento.todo.controller.dto.response.PrioritizationResponse;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.service.command.ToDoPrioritizationCommand;
import com.official.memento.todo.service.usecase.ToDoPrioritizationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos/prioritization")
@RequiredArgsConstructor
public class PrioritizationApiController implements PrioritizationApiDocs {

    private final ToDoPrioritizationUseCase toDoPrioritizationUseCase;

    @Override
    @PostMapping("/weekly")
    public ResponseEntity<SuccessResponse<PrioritizationResponse>> prioritizeWeeklyToDo(
            @Authorization AuthorizationUser authorizationUser,
            @RequestBody PrioritizationRequest request
    ) {
        List<List<ToDo>> todosForWeek = toDoPrioritizationUseCase.prioritizeWeekly(
                ToDoPrioritizationCommand.of(
                        authorizationUser.memberId(),
                        request.targetDate()
                )
        );

        return SuccessResponse.of(
                HttpStatus.OK,
                "일주일 AI 우선 순위 정렬",
                PrioritizationResponse.of(todosForWeek)
        );
    }

    @Override
    @PostMapping("/daily")
    public ResponseEntity<SuccessResponse<PrioritizationDailyResponse>> prioritizeDailyToDo(
            @Authorization AuthorizationUser authorizationUser,
            @RequestBody PrioritizationRequest request
    ) {
        List<ToDo> todosForDaily = toDoPrioritizationUseCase.prioritizeDaily(
                ToDoPrioritizationCommand.of(
                        authorizationUser.memberId(),
                        request.targetDate()
                )
        );

        return SuccessResponse.of(
                HttpStatus.OK,
                "데일리 AI 우선 순위 정렬",
                PrioritizationDailyResponse.of(todosForDaily)
        );
    }
}
