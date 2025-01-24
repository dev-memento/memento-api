package com.official.memento.todo.controller

import com.official.memento.global.annotation.Authorization
import com.official.memento.global.annotation.AuthorizationUser
import com.official.memento.global.dto.SuccessResponse
import com.official.memento.todo.controller.dto.PrioritizationDailyResponse
import com.official.memento.todo.controller.dto.PrioritizationRequest
import com.official.memento.todo.controller.dto.PrioritizationResponse
import com.official.memento.todo.service.ToDoPrioritizationUseCase
import com.official.memento.todo.service.command.ToDoPrioritizationCommand
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/todos/prioritization")
class PrioritizationApiController(
    private val toDoPrioritizationUseCase: ToDoPrioritizationUseCase,
) : PrioritizationApiDocs {
    @PostMapping("/weekly")
    override fun prioritizeWeeklyToDo(
        @Authorization authorizationUser: AuthorizationUser,
        @RequestBody request: PrioritizationRequest,
    ): ResponseEntity<SuccessResponse<PrioritizationResponse>> {
        val todosForWeek =
            toDoPrioritizationUseCase.prioritizeWeekly(
                ToDoPrioritizationCommand(
                    memberId = authorizationUser.memberId,
                    targetDate = request.targetDate,
                ),
            )
        return SuccessResponse.of(HttpStatus.OK, "일주일 AI 우선 순위 정렬", PrioritizationResponse.of(todosForWeek))
    }

    @PostMapping("/daily")
    fun prioritizeDailyToDo(
        @Authorization authorizationUser: AuthorizationUser,
        @RequestBody request: PrioritizationRequest,
    ): ResponseEntity<SuccessResponse<PrioritizationDailyResponse>> {
        val todosForDaily = toDoPrioritizationUseCase.prioritizeDaily(
                ToDoPrioritizationCommand(
                    memberId = authorizationUser.memberId,
                    targetDate = request.targetDate,
                ),
            )
        return SuccessResponse.of(HttpStatus.OK, "데일리 AI 우선 순위 정렬", PrioritizationDailyResponse.of(todosForDaily))
    }
}
