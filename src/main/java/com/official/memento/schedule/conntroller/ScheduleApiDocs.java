package com.official.memento.schedule.conntroller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.schedule.conntroller.dto.request.RepeatScheduleCreateRequest;
import com.official.memento.schedule.conntroller.dto.request.ScheduleCreateRequest;
import com.official.memento.schedule.conntroller.dto.request.ScheduleUpdateGroupRequest;
import com.official.memento.schedule.conntroller.dto.request.ScheduleUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "[Schedule] 일정 관련 API")
public interface ScheduleApiDocs {

    @Operation(summary = "일정 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "일정 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "일정 생성 실패")
            }
    )
    ResponseEntity<SuccessResponse<?>> createSchedule(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final ScheduleCreateRequest scheduleCreateRequest
    );

    @Operation(summary = "반복 일정 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "반복 일정 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "반복 일정 생성 실패")
            }
    )
    ResponseEntity<SuccessResponse<?>> createScheduleMultiple(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final RepeatScheduleCreateRequest repeatScheduleCreateRequest
    );

    @Operation(summary = "일정 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "일정 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "일정 삭제 실패")
            }
    )
    ResponseEntity<SuccessResponse<?>> deleteSchedule(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId
    );

    @Operation(summary = "일정 그룹 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "일정 그룹 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "일정 그룹 삭제 실패")
            }
    )
    ResponseEntity<SuccessResponse<?>> deleteScheduleGroup(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId,
            @RequestParam final String scheduleGroupId
    );

    @Operation(summary = "일정 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "일정 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "일정 수정 실패"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    ResponseEntity<SuccessResponse<?>> updateScheduleGroup(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId,
            @RequestBody final ScheduleUpdateGroupRequest scheduleUpdateGroupRequest
    );

    @Operation(summary = "일정 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "일정 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "일정 수정 실패"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    ResponseEntity<SuccessResponse<?>> updateSchedule(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId,
            @RequestBody final ScheduleUpdateRequest scheduleUpdateRequest
    );
}