package com.official.memento.schedule.conntroller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.schedule.conntroller.dto.request.RepeatScheduleCreateRequest;
import com.official.memento.schedule.conntroller.dto.request.ScheduleCreateRequest;
import com.official.memento.schedule.conntroller.dto.request.ScheduleUpdateGroupRequest;
import com.official.memento.schedule.conntroller.dto.request.ScheduleUpdateRequest;
import com.official.memento.schedule.conntroller.dto.response.ScheduleAllAllDaysGetResponse;
import com.official.memento.schedule.conntroller.dto.response.ScheduleAllGetResponse;
import com.official.memento.schedule.conntroller.dto.response.ScheduleDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

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
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
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
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final RepeatScheduleCreateRequest repeatScheduleCreateRequest
    );

    @Operation(summary = "Apple 일정 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Apple 일정 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "Apple 일정 생성 실패")
            }
    )
    ResponseEntity<SuccessResponse<?>> createAppleSchedules(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final List<ScheduleCreateRequest> request
    );

    @Operation(summary = "일정 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "일정 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "일정 삭제 실패")
            }
    )
    ResponseEntity<SuccessResponse<?>> deleteSchedule(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId
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
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId,
            @RequestBody final ScheduleUpdateRequest scheduleUpdateRequest
    );

    @Operation(summary = "특정 날짜의 일정 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "일정 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleAllGetResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    ResponseEntity<SuccessResponse<ScheduleAllGetResponse>> getSchedulesByDate(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @RequestParam final LocalDate date
    );

    @Operation(summary = "전체 일정 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "전체 일정 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleAllGetResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    ResponseEntity<SuccessResponse<ScheduleAllGetResponse>> getAllSchedules(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser
    );

    @Operation(summary = "모든 일정 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "모든 일정 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleAllAllDaysGetResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    ResponseEntity<SuccessResponse<ScheduleAllAllDaysGetResponse>> getAllDaysSchedules(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser
    );

    @Operation(summary = "일정 상세 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "일정 상세 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleDetailResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    ResponseEntity<SuccessResponse<ScheduleDetailResponse>> getDetailSchedules(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId
    );
}
