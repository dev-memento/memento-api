package com.official.memento.todo.controller

import com.official.memento.global.annotation.Authorization
import com.official.memento.global.annotation.AuthorizationUser
import com.official.memento.global.dto.SuccessResponse
import com.official.memento.todo.controller.dto.BrainDumpCreateRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "[BrainDump API] 브레인 덤프 API docs")
interface BrainDumpApiDocs {

    @Operation(summary = "브레인 덤프 투두를 생성합니다.", description = "브레인 덤프 투두를 생성합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "브레인 덤프 투두 생성 성공"),
        ApiResponse(responseCode = "500", description = "서버 에러"),
    )
    fun createBrainDump(
        @RequestBody request: BrainDumpCreateRequest,
        @Authorization authorizationUser: AuthorizationUser,
    ): ResponseEntity<SuccessResponse<*>>?
}