package com.official.memento.todo.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.todo.controller.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;


@Tag(name = "[ToDo API] 할 일 관련 API")
public interface ToDoApiDocs {

    @Operation(description = "ToDo 생성 API")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<SuccessResponse<?>> createToDo(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final ToDoCreateRequest request
    );

    @Operation(description = "ToDo 단일 삭제 API")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<SuccessResponse<?>> deleteToDo(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId
    );

    @Operation(description = "ToDo 단일 수정 API")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<SuccessResponse<?>> updateToDo(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId,
            @RequestBody final ToDoUpdateRequest request
    );

    @Operation(description = "Todo 완료 상태 수정 API")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<SuccessResponse<ToDoCompletionResponse>> updateToDoCompletion(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId
    );

    @Operation(description = "Todo 목록 조회 API")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<SuccessResponse<ToDoAllGetResponse>> getToDos(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser
    );

    @Operation(description = "ToDo 드래그앤드롭 API")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    ResponseEntity<SuccessResponse<?>> updateToDoPosition(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId,
            @RequestBody final ToDoDragAndDropRequest request
    );

    @Operation(description = "당일 ToDo Get API")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<SuccessResponse<ToDoAllGetResponse>> getTodoByDate(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @RequestParam final LocalDate date
    );

    @Operation(description = "ToDo 디테일 Get API")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<SuccessResponse<ToDoDetailGetResponse>> getDetailToDos(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId
    );
}
