package com.official.memento.schedule.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.global.util.Validator;
import com.official.memento.schedule.controller.dto.request.*;
import com.official.memento.schedule.controller.dto.response.ScheduleAllAllDaysGetResponse;
import com.official.memento.schedule.controller.dto.response.ScheduleAllGetResponse;
import com.official.memento.schedule.controller.dto.response.ScheduleDetailResponse;
import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.service.command.*;
import com.official.memento.schedule.service.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleApiController implements ScheduleApiDocs {

    private final ScheduleGetUseCase scheduleGetUseCase;
    private final ScheduleCreateUseCase scheduleCreateUseCase;
    private final ScheduleDeleteUseCase scheduleDeleteUseCase;
    private final ScheduleUpdateUseCase scheduleUpdateUseCase;
    private final ScheduleGroupDeleteUseCase scheduleGroupDeleteUseCase;
    private final ScheduleGroupUpdateUseCase scheduleGroupUpdateUseCase;
    private final ScheduleGroupCreateUseCase scheduleGroupCreateUseCase;

    @PostMapping
    @Override
    public ResponseEntity<SuccessResponse<?>> createSchedule(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final ScheduleCreateRequest scheduleCreateRequest
    ) {
        scheduleCreateUseCase.create(
                ScheduleCreateCommand.of(
                        authorizationUser.memberId(),
                        scheduleCreateRequest.description(),
                        scheduleCreateRequest.startDate(),
                        scheduleCreateRequest.endDate(),
                        scheduleCreateRequest.isAllDay(),
                        scheduleCreateRequest.tagId()
                )
        );
        return SuccessResponse.of(
                HttpStatus.CREATED,
                "단일 스케줄 생성 성공"
        );
    }

    @PostMapping("/repetition")
    @Override
    public ResponseEntity<SuccessResponse<?>> createScheduleMultiple(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final RepeatScheduleCreateRequest repeatScheduleCreateRequest
    ) {
        scheduleGroupCreateUseCase.createRepeat(
                RepeatScheduleCreateCommand.of(
                        authorizationUser.memberId(),
                        repeatScheduleCreateRequest.description(),
                        repeatScheduleCreateRequest.startDate(),
                        repeatScheduleCreateRequest.endDate(),
                        repeatScheduleCreateRequest.isAllDay(),
                        repeatScheduleCreateRequest.repeatOption(),
                        repeatScheduleCreateRequest.repeatExpiredDate(),
                        repeatScheduleCreateRequest.tagId()
                )
        );
        return SuccessResponse.of(
                HttpStatus.CREATED,
                "반복 스케줄 생성 성공"
        );
    }

    @Override
    @PostMapping("/apple")
    public ResponseEntity<SuccessResponse<?>> createAppleSchedules(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final AppleSchedulesRequest request
    ) {
        scheduleCreateUseCase.createAppleSchedules(
                AppleSchedulesCommand.of(
                        authorizationUser.memberId(),
                        request.syncToken(),
                        request.scheduleCreateRequest().stream().map(scheduleCreateRequest ->
                                AppleScheduleCreateCommand.of(
                                        scheduleCreateRequest.id(),
                                        scheduleCreateRequest.description(),
                                        scheduleCreateRequest.startDate(),
                                        scheduleCreateRequest.endDate(),
                                        scheduleCreateRequest.isAllDay()
                                )
                        ).toList()));
        return SuccessResponse.of(
                HttpStatus.CREATED,
                "애플 스케줄 생성 성공"
        );
    }

    @Override
    @PutMapping("/apple")
    public ResponseEntity<SuccessResponse<?>> syncAppleSchedules(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final AppleSchedulesRequest request
    ) {
        scheduleCreateUseCase.syncAppleSchedules(
                AppleSchedulesCommand.of(
                        authorizationUser.memberId(),
                        request.syncToken(),
                        request.scheduleCreateRequest().stream().map(scheduleCreateRequest ->
                                AppleScheduleCreateCommand.of(
                                        scheduleCreateRequest.id(),
                                        scheduleCreateRequest.description(),
                                        scheduleCreateRequest.startDate(),
                                        scheduleCreateRequest.endDate(),
                                        scheduleCreateRequest.isAllDay()
                                )
                        ).toList()));
        return SuccessResponse.of(
                HttpStatus.OK,
                "애플 스케줄 동기화 성공"
        );
    }

    @PutMapping("/google")
    public ResponseEntity<SuccessResponse<?>> syncGoogleSchedules(
            @Authorization final AuthorizationUser authorizationUser
    ) {
        scheduleCreateUseCase.syncGoogleSchedules(authorizationUser.memberId());
        return SuccessResponse.of(
                HttpStatus.CREATED,
                "구글 스케줄 동기화 성공"
        );
    }

    @DeleteMapping("/{scheduleId}")
    @Override
    public ResponseEntity<SuccessResponse<?>> deleteSchedule(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId
    ) {
        scheduleDeleteUseCase.delete(ScheduleDeleteCommand.of(authorizationUser.memberId(), scheduleId));
        return SuccessResponse.of(
                HttpStatus.OK,
                "단일 스케줄 삭제 성공"
        );
    }

    @DeleteMapping("/{scheduleId}/group")
    public ResponseEntity<SuccessResponse<?>> deleteScheduleGroup(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId,
            @RequestParam final String scheduleGroupId
    ) {
        scheduleGroupDeleteUseCase.deleteGroup(
                ScheduleDeleteGroupCommand.of(authorizationUser.memberId(), scheduleId, scheduleGroupId));
        return SuccessResponse.of(
                HttpStatus.OK,
                "반복 스케줄 삭제 성공"
        );
    }

    @PatchMapping("/{scheduleId}")
    @Override
    public ResponseEntity<SuccessResponse<?>> updateSchedule(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId,
            @RequestBody final ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        scheduleUpdateUseCase.update(ScheduleUpdateCommand.of(
                authorizationUser.memberId(),
                scheduleId,
                scheduleUpdateRequest.description(),
                scheduleUpdateRequest.startDate(),
                scheduleUpdateRequest.endDate(),
                scheduleUpdateRequest.isAllDay(),
                scheduleUpdateRequest.tagId(),
                scheduleUpdateRequest.repeatOption(),
                scheduleUpdateRequest.repeatEndDate()
        ));
        return SuccessResponse.of(
                HttpStatus.OK,
                "단일 스케줄 업데이트 성공"
        );
    }

    @PatchMapping("/{scheduleId}/google")
    public ResponseEntity<SuccessResponse<?>> updateGoogleSchedule(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId,
            @RequestBody final ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        scheduleUpdateUseCase.updateGoogle(ScheduleUpdateCommand.of(
                authorizationUser.memberId(),
                scheduleId,
                scheduleUpdateRequest.description(),
                scheduleUpdateRequest.startDate(),
                scheduleUpdateRequest.endDate(),
                scheduleUpdateRequest.isAllDay(),
                scheduleUpdateRequest.tagId(),
                scheduleUpdateRequest.repeatOption(),
                scheduleUpdateRequest.repeatEndDate()
        ));
        return SuccessResponse.of(
                HttpStatus.OK,
                "구글 스케줄 업데이트 성공"
        );
    }

    @PatchMapping("/{scheduleId}/group")
    public ResponseEntity<SuccessResponse<?>> updateScheduleGroup(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long scheduleId,
            @RequestBody final ScheduleUpdateGroupRequest scheduleUpdateGroupRequest
    ) {
        scheduleGroupUpdateUseCase.updateGroup(ScheduleUpdateGroupCommand.of(
                authorizationUser.memberId(),
                scheduleId,
                scheduleUpdateGroupRequest.description(),
                scheduleUpdateGroupRequest.startDate(),
                scheduleUpdateGroupRequest.endDate(),
                scheduleUpdateGroupRequest.repeatOption(),
                scheduleUpdateGroupRequest.repeatExpiredDate(),
                scheduleUpdateGroupRequest.isAllDay(),
                scheduleUpdateGroupRequest.tagId(),
                scheduleUpdateGroupRequest.scheduleGroupId()
        ));
        return SuccessResponse.of(
                HttpStatus.OK,
                "반복 스케줄 업데이트 성공"
        );
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<ScheduleAllGetResponse>> getSchedulesByDate(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestParam LocalDate date
    ) {
        Validator.isNull(date);
        List<Schedule> schedules = scheduleGetUseCase.getSchedules(authorizationUser.memberId(), date);

        return SuccessResponse.of(
                HttpStatus.CREATED,
                "스케줄 반환 성공",
                ScheduleAllGetResponse.of(schedules)
        );
    }


    @GetMapping("/total")
    public ResponseEntity<SuccessResponse<ScheduleAllGetResponse>> getAllSchedules(
            @Authorization final AuthorizationUser authorizationUser
    ) {
        List<Schedule> allSchedules = scheduleGetUseCase.getAllSchedules(authorizationUser.memberId());
        return SuccessResponse.of(
                HttpStatus.OK,
                "전체 스케줄 조회 성공",
                ScheduleAllGetResponse.of(allSchedules)
        );
    }

    @GetMapping("/all-days")
    public ResponseEntity<SuccessResponse<ScheduleAllAllDaysGetResponse>> getAllDaysSchedules(
            @Authorization final AuthorizationUser authorizationUser
    ) {
        List<Schedule> allSchedules = scheduleGetUseCase.getAllAllDaysSchedules(authorizationUser.memberId());
        return SuccessResponse.of(
                HttpStatus.OK,
                "스케줄 조회 성공",
                ScheduleAllAllDaysGetResponse.of(allSchedules)
        );
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<SuccessResponse<ScheduleDetailResponse>> getDetailSchedules(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable long scheduleId
    ) {
        Schedule schedule = scheduleGetUseCase.getDetail(authorizationUser.memberId(), scheduleId);
        return SuccessResponse.of(
                HttpStatus.OK,
                "스케줄 조회 성공",
                ScheduleDetailResponse.of(schedule)
        );
    }
}