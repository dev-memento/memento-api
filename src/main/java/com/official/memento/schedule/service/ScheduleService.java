package com.official.memento.schedule.service;

import com.official.memento.auth.infrastructure.google.GoogleAuthClientAdapter;
import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.member.service.command.MemberSyncInfoGetUseCase;
import com.official.memento.member.service.result.MemberSyncInfoResult;
import com.official.memento.member.service.usecase.MemberSyncInfoUpdateUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoCreateUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoDeleteUseCase;
import com.official.memento.schedule.domain.ScheduleRepository;
import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.entity.ScheduleVo;
import com.official.memento.schedule.infrastructure.google.GoogleCalendarAdapter;
import com.official.memento.schedule.infrastructure.google.GoogleCalendarEvent;
import com.official.memento.schedule.infrastructure.google.GoogleCalendarResponse;
import com.official.memento.schedule.service.command.AppleScheduleCreateCommand;
import com.official.memento.schedule.service.command.AppleSchedulesCommand;
import com.official.memento.schedule.service.command.RepeatScheduleCreateCommand;
import com.official.memento.schedule.service.command.ScheduleCreateCommand;
import com.official.memento.schedule.service.command.ScheduleDeleteCommand;
import com.official.memento.schedule.service.command.ScheduleDeleteGroupCommand;
import com.official.memento.schedule.service.command.ScheduleUpdateCommand;
import com.official.memento.schedule.service.command.ScheduleUpdateGroupCommand;
import com.official.memento.schedule.service.usecase.ScheduleCreateUseCase;
import com.official.memento.schedule.service.usecase.ScheduleDeleteUseCase;
import com.official.memento.schedule.service.usecase.ScheduleGetUseCase;
import com.official.memento.schedule.service.usecase.ScheduleGroupCreateUseCase;
import com.official.memento.schedule.service.usecase.ScheduleGroupDeleteUseCase;
import com.official.memento.schedule.service.usecase.ScheduleGroupUpdateUseCase;
import com.official.memento.schedule.service.usecase.ScheduleUpdateUseCase;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.tag.service.TagGetUseCase;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.official.memento.schedule.domain.enums.ScheduleType.*;

@Service
@RequiredArgsConstructor
public class ScheduleService implements
        ScheduleCreateUseCase,
        ScheduleDeleteUseCase,
        ScheduleUpdateUseCase,
        ScheduleGetUseCase,
        ScheduleGroupCreateUseCase,
        ScheduleGroupDeleteUseCase,
        ScheduleGroupUpdateUseCase {
    private final ScheduleRepository scheduleRepository;
    private final TagGetUseCase tagGetUseCase;
    private final OrderInfoDeleteUseCase orderInfoDeleteUseCase;
    private final OrderInfoCreateUseCase orderInfoCreateUseCase;
    private final MemberSyncInfoUpdateUseCase memberSyncInfoUpdateUseCase;
    private final MemberSyncInfoGetUseCase memberSyncInfoGetUseCase;
    private final GoogleCalendarAdapter googleCalendarAdapter;
    private final GoogleAuthClientAdapter googleAuthClientAdapter;

    @Override
    @Transactional
    public void create(final ScheduleCreateCommand command) {
        Tag tag = tagGetUseCase.findById(command.tagId());
        checkOwnTag(command.memberId(), tag);
        checkDate(command.startDate(),command.endDate());
        Schedule schedule = createSchedule(command);
        orderInfoCreateUseCase.assignScheduleOrder(command.startDate().toLocalDate(), schedule, command.memberId());
    }

    @Override
    public void createAppleSchedules(final AppleSchedulesCommand command) {
        memberSyncInfoUpdateUseCase.activateAppleSync(command.memberId());
        Tag tag = tagGetUseCase.findByMemberIdAndTagColor(command.memberId(), TagColor.GRAY05);
        Map<String, ScheduleVo> newScheduleMap = getScheduleVoMap(command);
        for (String addedScheduleGroupId : newScheduleMap.keySet()) {
            createAppleSchedule(command.memberId(), tag.getId(), newScheduleMap.get(addedScheduleGroupId));
        }
    }

    @Override
    public void syncAppleSchedules(final AppleSchedulesCommand command) {
        MemberSyncInfoResult memberSyncInfo = memberSyncInfoGetUseCase.findByMemberId(command.memberId());
        Tag tag = tagGetUseCase.findByMemberIdAndTagColor(command.memberId(), TagColor.GRAY05);
        Map<String, ScheduleVo> newScheduleMap = getScheduleVoMap(command);
        if (memberSyncInfo.isAppleSync()) {
            List<Schedule> existSchedules = scheduleRepository.findAllAppleByMemberId(command.memberId());
            Map<String, ScheduleVo> existingScheduleMap = getScheduleVoMap(existSchedules);

            Set<String> allScheduleIds = getAllScheduleIds(existingScheduleMap, newScheduleMap);

            List<String> addedScheduleGroupIds = new ArrayList<>();
            List<String> removeScheduleGroupIds = new ArrayList<>();
            List<String> updatedScheduleGroupIds = new ArrayList<>();

            for (String eventId : allScheduleIds) {
                boolean existsInOld = existingScheduleMap.containsKey(eventId);
                boolean existsInNew = newScheduleMap.containsKey(eventId);
                if (!existsInOld && existsInNew) {
                    addedScheduleGroupIds.add(eventId);
                } else if (existsInOld && !existsInNew) {
                    removeScheduleGroupIds.add(eventId);
                } else if (existsInOld && existsInNew) {
                    if (!existingScheduleMap.get(eventId).equals(newScheduleMap.get(eventId))) {
                        updatedScheduleGroupIds.add(eventId);
                    }
                }
            }

            for (String addedScheduleGroupId : addedScheduleGroupIds) {
                createAppleSchedule(command.memberId(), tag.getId(), newScheduleMap.get(addedScheduleGroupId));
            }
            for (String removeScheduleGroupId : removeScheduleGroupIds) {
                deleteAppleSchedule(removeScheduleGroupId);
            }
            for (String updatedScheduleGroupId : updatedScheduleGroupIds) {
                updateAppleSchedule(newScheduleMap.get(updatedScheduleGroupId));
            }
        } else {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_REQUEST_BODY);
        }
    }

    @Override
    public void syncGoogleSchedules(final long memberId) {
        MemberSyncInfoResult memberSyncInfo = memberSyncInfoGetUseCase.findByMemberId(memberId);
        String accessToken = googleAuthClientAdapter.refreshAccessToken(memberSyncInfo.googleSyncToken());

        GoogleCalendarResponse googleCalendarEvents = googleCalendarAdapter.getCalendarEvents(accessToken,
                memberSyncInfo.googleSyncToken());
        memberSyncInfoUpdateUseCase.updateGoogleSyncToken(memberId, googleCalendarEvents.nextSyncToken());

        List<Schedule> createOrNormalUpdateSchedules = new ArrayList<>();
        List<String> dateUpdateSchedules = new ArrayList<>();
        List<String> deleteSchedule = new ArrayList<>();

        Tag tag = tagGetUseCase.findByMemberIdAndTagColor(memberId, TagColor.GRAY05);
        for (GoogleCalendarEvent event : googleCalendarEvents.items()) {
            if (event.status().equals("cancelled")) {
                deleteSchedule.add(event.id());
            }
            if (event.status().equals("confirmed")) {
                Schedule newSchedule = event.toSchedule(memberId, tag.getId());
                scheduleRepository.findNullableByScheduleGroupId(event.id()).ifPresentOrElse(
                        schedule -> {
                            if (!schedule.getStartDate().equals(newSchedule.getStartDate())) {
                                dateUpdateSchedules.add(event.id());
                            }
                            schedule.update(
                                    newSchedule.getDescription(),
                                    newSchedule.getStartDate(),
                                    newSchedule.getEndDate(),
                                    newSchedule.isAllDay(),
                                    newSchedule.getTagId(),
                                    newSchedule.getRepeatOption(),
                                    newSchedule.getRepeatExpiredDate()
                            );
                            createOrNormalUpdateSchedules.add(schedule);
                        },
                        () -> createOrNormalUpdateSchedules.add(newSchedule)
                );
            }
        }

        for (String deleteScheduleId : deleteSchedule) {
            scheduleRepository.findNullableByScheduleGroupId(deleteScheduleId).ifPresent(schedule -> {
                scheduleRepository.deleteAllByScheduleGroupId(deleteScheduleId);
                orderInfoDeleteUseCase.deleteByScheduleId(schedule.getId());
            });
        }
        for (String dateUpdateScheduleId : dateUpdateSchedules) {
            scheduleRepository.findNullableByScheduleGroupId(dateUpdateScheduleId).ifPresent(schedule -> {
                orderInfoDeleteUseCase.deleteByScheduleId(schedule.getId());
            });
        }
        for (Schedule schedule : createOrNormalUpdateSchedules) {
            Schedule saved = scheduleRepository.save(schedule);
            orderInfoCreateUseCase.assignScheduleOrder(saved.getStartDate().toLocalDate(), saved, memberId);
        }
    }

    @Override
    @Transactional
    public ScheduleResult update(final ScheduleUpdateCommand command) {
        Schedule schedule = scheduleRepository.findById(command.scheduleId());
        checkOwn(command.memberId(), schedule);

        if (command.tagId() != schedule.getTagId()) {
            checkOwnTag(command.memberId(), tagGetUseCase.findById(command.tagId()));
        }

        if(command.startDate() != schedule.getStartDate() || command.endDate() != schedule.getEndDate()){
            checkDate(command.startDate(),command.endDate());
        }

        schedule.update(
                command.description(),
                command.startDate(),
                command.endDate(),
                command.isAllDay(),
                command.tagId(),
                command.repeatOption(),
                command.repeatEndDate()
        );
        scheduleRepository.update(schedule);

        if (schedule.getStartDate() != command.startDate() || schedule.getEndDate() != command.endDate()) {
            orderInfoDeleteUseCase.deleteByScheduleId(schedule.getId());
            orderInfoCreateUseCase.assignScheduleOrder(command.startDate().toLocalDate(), schedule, command.memberId());

        }
        return ScheduleResult.of(schedule);
    }

    @Override
    @Transactional
    public void updateGoogle(final ScheduleUpdateCommand command) {
        ScheduleResult scheduleResult = update(command);
        MemberSyncInfoResult memberSyncInfo = memberSyncInfoGetUseCase.findByMemberId(command.memberId());
        String accessToken = googleAuthClientAdapter.refreshAccessToken(memberSyncInfo.googleSyncToken());
        Schedule schedule = Schedule.of(
                command.memberId(),
                scheduleResult.description(),
                scheduleResult.startDate(),
                scheduleResult.endDate(),
                scheduleResult.isAllDay(),
                scheduleResult.repeatOption(),
                scheduleResult.repeatEndDate(),
                GOOGLE,
                scheduleResult.scheduleGroupId(),
                scheduleResult.tagId()
        );
        googleCalendarAdapter.updateCalendarEvent(accessToken,schedule);
    }

    @Override
    @Transactional
    public void delete(final ScheduleDeleteCommand command) {
        Schedule schedule = scheduleRepository.findById(command.scheduleId());
        checkOwn(command.memberId(), schedule);
        scheduleRepository.deleteById(schedule.getId());
        orderInfoDeleteUseCase.deleteByScheduleId(schedule.getId());
    }

    @Override
    @Transactional
    public void createRepeat(final RepeatScheduleCreateCommand command) {
        String scheduleGroupId = UUID.randomUUID().toString();
        List<Schedule> schedules = createRepeatSchedules(
                command.memberId(),
                command.description(),
                command.startDate(),
                command.endDate(),
                command.repeatOption(),
                command.repeatExpiredDate(),
                command.isAllDay(),
                scheduleGroupId,
                command.tagId()
        );
        //Todo 기능 미구현
    }

    @Override
    @Transactional
    public void deleteGroup(final ScheduleDeleteGroupCommand command) {
        Schedule schedule = scheduleRepository.findById(command.scheduleId());
        checkOwn(command.memberId(), schedule);
        belongsToGroup(command.scheduleGroupId(), schedule);
        List<Schedule> targetSchedules = scheduleRepository.findAllByScheduleGroupIdAndStartDateGreaterThanEqual(
                command.scheduleGroupId(), schedule.getStartDate());
        scheduleRepository.deleteAll(targetSchedules);
        //Todo 기능 미구현
    }

    @Override
    @Transactional
    public void updateGroup(final ScheduleUpdateGroupCommand command) {
        Schedule schedule = scheduleRepository.findById(command.scheduleId());
        checkOwn(command.memberId(), schedule);
        belongsToGroup(command.scheduleGroupId(), schedule);
        List<Schedule> oldSchedules = scheduleRepository.findAllByScheduleGroupIdAndStartDateGreaterThanEqual(
                command.scheduleGroupId(),
                schedule.getStartDate()
        );
        List<Schedule> newSchedules = createRepeatSchedules(
                command.memberId(),
                command.description(),
                command.startDate(),
                command.endDate(),
                command.repeatOption(),
                command.repeatExpiredDate(),
                command.isAllDay(),
                schedule.getScheduleGroupId(),
                command.tagId()
        );
        //Todo 기능 미구현
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getAllSchedules(final long memberId) {
        return scheduleRepository.findNonAllDaySchedulesWithOrderInfo(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getAllAllDaysSchedules(final long memberId) {
        return scheduleRepository.findAllAlDaysByMemberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Schedule getDetail(final long memberId, final long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId);
        checkOwn(memberId, schedule);
        return schedule;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getSchedules(final long memberId, final LocalDate date) {
        return scheduleRepository.findAllByStartDateAndMemberId(date, memberId);
    }

    private static void checkOwnTag(final long memberId, final Tag tag) {
        if (tag.getMemberId() != memberId) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_REQUEST_BODY);
        }
    }

    private static void checkDate(final LocalDateTime startDate, final LocalDateTime endDate){
        if (endDate.isBefore(startDate)) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_REQUEST_BODY);
        }
    }

    private Schedule createSchedule(final ScheduleCreateCommand command) {
        String scheduleGroupId = UUID.randomUUID().toString();
        return scheduleRepository.save(Schedule.of(
                command.memberId(),
                command.description(),
                command.startDate(),
                command.endDate(),
                command.isAllDay(),
                RepeatOption.NONE,
                null,
                NORMAL,
                scheduleGroupId,
                command.tagId()
        ));
    }

    private Schedule createAppleSchedule(final AppleScheduleCreateCommand command, final long memberId,
                                         final long tagId) {
        return scheduleRepository.save(Schedule.of(
                memberId,
                command.description(),
                command.startDate(),
                command.endDate(),
                command.isAllDay(),
                RepeatOption.NONE,
                null,
                APPLE,
                command.scheduleUniqueId(),
                tagId
        ));
    }

    @NotNull
    private static Set<String> getAllScheduleIds(
            final Map<String, ScheduleVo> existingScheduleMap,
            final Map<String, ScheduleVo> newScheduleMap
    ) {
        Set<String> allScheduleIds = new HashSet<>();
        allScheduleIds.addAll(existingScheduleMap.keySet());
        allScheduleIds.addAll(newScheduleMap.keySet());
        return allScheduleIds;
    }

    @NotNull
    private static Map<String, ScheduleVo> getScheduleVoMap(final AppleSchedulesCommand command) {
        Map<String, ScheduleVo> newScheduleMap = new HashMap<>();
        for (AppleScheduleCreateCommand schedule : command.commands()) {
            newScheduleMap.put(schedule.scheduleUniqueId() + schedule.startDate() + schedule.endDate(),
                    ScheduleVo.of(
                            schedule.description(),
                            schedule.startDate(),
                            schedule.endDate(),
                            schedule.scheduleUniqueId() + schedule.startDate() + schedule.endDate(),
                            schedule.isAllDay(),
                            APPLE
                    ));
        }
        return newScheduleMap;
    }

    @NotNull
    private static Map<String, ScheduleVo> getScheduleVoMap(final List<Schedule> existSchedules) {
        Map<String, ScheduleVo> existingScheduleMap = new HashMap<>();
        for (Schedule schedule : existSchedules) {
            existingScheduleMap.put(schedule.getScheduleGroupId(),
                    ScheduleVo.of(
                            schedule.getDescription(),
                            schedule.getStartDate(),
                            schedule.getEndDate(),
                            schedule.getScheduleGroupId(),
                            schedule.isAllDay(),
                            schedule.getType()
                    ));
        }
        return existingScheduleMap;
    }

    private void updateAppleSchedule(final ScheduleVo scheduleVo) {
        Optional<Schedule> schedule = scheduleRepository.findNullableByScheduleGroupId(scheduleVo.scheduleGroupId());
        if (schedule.isPresent()) {
            schedule.get().update(scheduleVo.description(), scheduleVo.startDate(), scheduleVo.endDate(),
                    scheduleVo.isAllDay(),
                    schedule.get().getTagId(), schedule.get().getRepeatOption(), schedule.get().getRepeatExpiredDate());
            scheduleRepository.save(schedule.get());
        }

    }

    private void deleteAppleSchedule(final String removeScheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findNullableByScheduleGroupId(removeScheduleId);
        if (schedule.isPresent()) {
            scheduleRepository.deleteAllByScheduleGroupId(removeScheduleId);
            orderInfoDeleteUseCase.deleteByScheduleId(schedule.get().getId());
        }
    }

    private void createAppleSchedule(
            final long memberId,
            final long tagId,
            final ScheduleVo scheduleVo
    ) {
        Schedule schedule = scheduleRepository.save(Schedule.of(
                memberId,
                scheduleVo.description(),
                scheduleVo.startDate(),
                scheduleVo.endDate(),
                scheduleVo.isAllDay(),
                RepeatOption.NONE,
                null,
                APPLE,
                scheduleVo.scheduleGroupId(),
                tagId
        ));
        orderInfoCreateUseCase.assignScheduleOrder(schedule.getStartDate().toLocalDate(), schedule,
                memberId);
    }


    private List<Schedule> createRepeatSchedules(
            final long memberId,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final boolean isAllDay,
            final String scheduleGroupId,
            final long tagId
    ) {
        List<Schedule> schedules = new ArrayList<>();
        LocalDateTime currentStartDate = startDate;
        LocalDateTime currentEndDate = endDate;
        while (!currentEndDate.toLocalDate().isAfter(repeatExpiredDate)) {
            Schedule schedule = scheduleRepository.save(Schedule.of(
                    memberId,
                    description,
                    currentStartDate,
                    currentEndDate,
                    isAllDay,
                    repeatOption,
                    repeatExpiredDate,
                    NORMAL,
                    scheduleGroupId,
                    tagId
            ));
            schedules.add(schedule);
            switch (repeatOption) {
                case DAILY -> {
                    currentStartDate = currentStartDate.plusDays(1);
                    currentEndDate = currentEndDate.plusDays(1);
                }
                case WEEKLY -> {
                    currentStartDate = currentStartDate.plusWeeks(1);
                    currentEndDate = currentEndDate.plusWeeks(1);
                }
                case MONTHLY -> {
                    currentStartDate = currentStartDate.plusMonths(1);
                    currentEndDate = currentEndDate.plusMonths(1);
                }
                case YEARLY -> {
                    currentStartDate = currentStartDate.plusYears(1);
                    currentEndDate = currentEndDate.plusYears(1);
                }
                default -> throw new IllegalArgumentException(
                        "Unsupported repeat option: " + repeatOption);//Todo 커스텀 익셉션으로 교체 예정
            }
        }
        return schedules;
        //Todo 기능 미구현
    }

    private static void checkOwn(final long memberId, final Schedule schedule) { //Todo Schedule안으로 리팩토링
        if (schedule.getMemberId() != memberId) {
            throw new IllegalArgumentException("해당 스케줄을 소유하지 않음");//Todo 커스텀으로 바꿔야함
        }
    }

    private static void belongsToGroup(final String scheduleGroupId, final Schedule schedule) { //Todo 스케줄 안으로 리팩토링
        if (!schedule.getScheduleGroupId().equals(scheduleGroupId)) {
            throw new IllegalArgumentException("해당 스케줄의 그룹 아이디와 일치하지 않습니다."); //Todo 커스텀
        }
    }
}
