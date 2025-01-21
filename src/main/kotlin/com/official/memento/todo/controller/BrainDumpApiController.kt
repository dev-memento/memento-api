package com.official.memento.todo.controller

import com.official.memento.global.annotation.Authorization
import com.official.memento.global.annotation.AuthorizationUser
import com.official.memento.global.dto.SuccessResponse
import com.official.memento.todo.controller.dto.BrainDumpCreateRequest
import com.official.memento.todo.service.BrainDumpCreateUseCase
import com.official.memento.todo.service.command.BrainDumpCreateCommand
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class BrainDumpApiController(
    private val brainDumpCreateUseCase: BrainDumpCreateUseCase,
) {
    @PostMapping("/braindump")
    fun createBrainDump(
        @RequestBody request: BrainDumpCreateRequest,
        @Authorization authorizationUser: AuthorizationUser,
    ): ResponseEntity<SuccessResponse<*>>? {
        brainDumpCreateUseCase.create(
            BrainDumpCreateCommand(
                authorizationUser.memberId,
                request.content,
            ),
        )
        return SuccessResponse.of(
            HttpStatus.OK,
            "브레인 덤프 투두 생성 성공.",
        )
    }
}
