package com.official.memento.todo.controller

import com.official.memento.global.annotation.Authorization
import com.official.memento.global.annotation.AuthorizationUser
import com.official.memento.global.dto.SuccessResponse
import com.official.memento.todo.controller.dto.PrioritizationDailyResponse
import com.official.memento.todo.controller.dto.PrioritizationRequest
import com.official.memento.todo.controller.dto.PrioritizationResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "[ToDo Prioritization API] 할 일 우선순위 API docs")
interface PrioritizationApiDocs {

    @Operation(summary = "주간 할 일 우선순위를 정렬합니다.", description = "주간 할 일 우선순위를 정렬합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "주간 할 일 우선순위 정렬 성공"),
        ApiResponse(responseCode = "400", description = "targetDate 잘못 요청 보낸 경우"),
        ApiResponse(responseCode = "500", description = "서버 에러"),
    )
    fun prioritizeWeeklyToDo(
        @Parameter(
            name = "Authorization",
            `in` = ParameterIn.HEADER,
            description = "Bearer accesstoken",
            required = true,
            example = "Bearer accesstoken"
        )
        @Authorization authorizationUser: AuthorizationUser,
        @RequestBody request: PrioritizationRequest,
    ): ResponseEntity<SuccessResponse<PrioritizationResponse>>

    @Operation(summary = "데일리 todo 우선순위 정렬 API.", description = "데일리로 할 일 우선순위를 정렬합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "데일리 할 일 우선순위 정렬 성공"),
        ApiResponse(responseCode = "400", description = "targetDate 잘못 요청 보낸 경우"),
        ApiResponse(responseCode = "500", description = "서버 에러"),
    )
    fun prioritizeDailyToDo(
        @Parameter(
            name = "Authorization",
            `in` = ParameterIn.HEADER,
            description = "Bearer accesstoken",
            required = true,
            example = "Bearer accesstoken"
        )
        @Authorization authorizationUser: AuthorizationUser,
        @RequestBody request: PrioritizationRequest,
    ): ResponseEntity<SuccessResponse<PrioritizationDailyResponse>>
}
