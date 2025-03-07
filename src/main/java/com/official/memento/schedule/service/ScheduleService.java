package com.official.memento.schedule.service;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.member.domain.MemberSyncInfo;
import com.official.memento.member.service.command.MemberSyncInfoGetUseCase;
import com.official.memento.member.service.usecase.MemberSyncInfoUpdateUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoCreateUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoDeleteUseCase;
import com.official.memento.schedule.domain.ScheduleRepository;
import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.entity.ScheduleVo;
import com.official.memento.schedule.service.command.*;
import com.official.memento.schedule.service.usecase.*;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.tag.service.TagGetUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.official.memento.schedule.domain.enums.ScheduleType.APPLE;
import static com.official.memento.schedule.domain.enums.ScheduleType.NORMAL;

@Service
@RequiredArgsConstructor
public class ScheduleService implements
        ScheduleCreateUseCase,
        ScheduleDeleteUseCase,
        ScheduleUpdateUseCase,
        ScheduleGetUseCase,
        ScheduleGroupCreateUseCase,
        ScheduleGroupDeleteUseCase,
        ScheduleGroupUpdateUseCase
{
    private final ScheduleRepository scheduleRepository;
    private final TagGetUseCase tagGetUseCase;
    private final OrderInfoDeleteUseCase orderInfoDeleteUseCase;
    private final OrderInfoCreateUseCase orderInfoCreateUseCase;
    private final MemberSyncInfoUpdateUseCase memberSyncInfoUpdateUseCase;
    private final MemberSyncInfoGetUseCase memberSyncInfoGetUseCase;

    @Override
    @Transactional
    public void create(final ScheduleCreateCommand command) {
        Tag tag = tagGetUseCase.findById(command.tagId());
        checkOwnTag(command.memberId(), tag);
        Schedule schedule = createSchedule(command);
        orderInfoCreateUseCase.assignScheduleOrder(command.startDate().toLocalDate(), schedule, command.memberId());
    }

    @Override
    public void createAppleSchedules(final AppleSchedulesCommand command) {
        memberSyncInfoUpdateUseCase.updateAppleToken(command.memberId(), command.syncToken());
        Tag tag = tagGetUseCase.findByMemberIdAndTagColor(command.memberId(), TagColor.GRAY05);
        for (AppleScheduleCreateCommand scheduleCreateCommand : command.commands()) {
            Schedule schedule = createAppleSchedule(scheduleCreateCommand, command.memberId(), tag.getId());
            orderInfoCreateUseCase.assignScheduleOrder(scheduleCreateCommand.startDate().toLocalDate(), schedule, command.memberId());
        }
    }

    @Override
    public void updateAppleSchedules(final AppleSchedulesCommand command) {
        MemberSyncInfo memberSyncInfo = memberSyncInfoGetUseCase.findByMemberId(command.memberId());
        if(!memberSyncInfo.getAppleSyncToken().equals(command.syncToken())){
            Tag tag = tagGetUseCase.findByMemberIdAndTagColor(command.memberId(), TagColor.GRAY05);
            memberSyncInfoUpdateUseCase.updateAppleToken(command.memberId(), command.syncToken());
            List<Schedule> existSchedules = scheduleRepository.findAllAppleByMemberId(command.memberId());

            Map<String, ScheduleVo> existingScheduletMap = new HashMap<>();
            for (Schedule schedule : existSchedules) {
                existingScheduletMap.put(schedule.getScheduleGroupId(),
                        ScheduleVo.of(
                                schedule.getDescription(),
                                schedule.getStartDate(),
                                schedule.getEndDate(),
                                schedule.getScheduleGroupId(),
                                schedule.getType()
                        ));
            }
            Map<String, ScheduleVo> newScheduletMap = new HashMap<>();
            for (AppleScheduleCreateCommand schedule : command.commands()) {
                newScheduletMap.put(schedule.scheduleUniqueId(), ScheduleVo.of(
                        schedule.description(),
                        schedule.startDate(),
                        schedule.endDate(),
                        schedule.scheduleUniqueId(),
                        APPLE
                ));
            }

            Set<String> allScheduleIds = new HashSet<>();
            allScheduleIds.addAll(existingScheduletMap.keySet());
            allScheduleIds.addAll(newScheduletMap.keySet());

            List<String> addedScheduleIds = new ArrayList<>();
            List<String> removeScheduleIds = new ArrayList<>();
            List<String> updatedScheduleIds = new ArrayList<>();


            for (String eventId : allScheduleIds) {
                boolean existsInOld = existingScheduletMap.containsKey(eventId);
                boolean existsInNew = newScheduletMap.containsKey(eventId);

                if (!existsInOld && existsInNew) {
                    // 추가된 이벤트
                    addedScheduleIds.add(eventId);
                } else if (existsInOld && !existsInNew) {
                    // 삭제된 이벤트
                    removeScheduleIds.add(eventId);
                } else if (existsInOld && existsInNew) {
                    // 수정된 이벤트 (ID는 동일하지만 데이터가 다름)
                    if (!existingScheduletMap.get(eventId).equals(newScheduletMap.get(eventId))) {
                        updatedScheduleIds.add(eventId);
                    }
                }
            }

            addNewAppleSchedules(command, addedScheduleIds, newScheduletMap, tag);
            deleteExistAppleSchedules(removeScheduleIds);

            //Todo 수정된 이벤트 처리


        }
    }

    private void deleteExistAppleSchedules(List<String> removeScheduleIds) {
        for(String removeScheduleId : removeScheduleIds){
            List<Schedule> schedules = scheduleRepository.findAllByScheduleGroupId(removeScheduleId);
            scheduleRepository.deleteAllByScheduleGroupId(removeScheduleId);
            for(Schedule schedule : schedules){
                orderInfoDeleteUseCase.deleteByScheduleId(schedule.getId());
            }
        }
    }

    private void addNewAppleSchedules(AppleSchedulesCommand command, List<String> addedScheduleIds, Map<String, ScheduleVo> newScheduletMap, Tag tag) {
        for(String addedScheduleId : addedScheduleIds){
            Schedule schedule = scheduleRepository.save(Schedule.of(
                    command.memberId(),
                    newScheduletMap.get(addedScheduleId).description(),
                    newScheduletMap.get(addedScheduleId).startDate(),
                    newScheduletMap.get(addedScheduleId).endDate(),
                    false,
                    RepeatOption.NONE,
                    null,
                    APPLE,
                    addedScheduleId,
                    tag.getId()
            ));
            orderInfoCreateUseCase.assignScheduleOrder(schedule.getStartDate().toLocalDate(), schedule, command.memberId());
        }
    }

    @Override
    public void createGoogleSchedules(String command) {

    }

    @Override
    @Transactional
    public void update(final ScheduleUpdateCommand command) {
        Schedule schedule = scheduleRepository.findById(command.scheduleId());
        checkOwn(command.memberId(), schedule);

        if (command.tagId() != schedule.getTagId()) {
            checkOwnTag(command.memberId(), tagGetUseCase.findById(command.tagId()));
        }

        schedule.update(
                command.description(),
                command.startDate(),
                command.endDate(),
                command.isAllDay(),
                command.tagId()
        );
        scheduleRepository.update(schedule);

        if (schedule.getStartDate() != command.startDate() || schedule.getEndDate() != command.endDate()) {
            orderInfoDeleteUseCase.deleteByScheduleId(schedule.getId());
            orderInfoCreateUseCase.assignScheduleOrder(command.startDate().toLocalDate(), schedule, command.memberId());

        }
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
    public void updateGroup(ScheduleUpdateGroupCommand command) {
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
    public List<Schedule> getAllAllDaysSchedules(long memberId) {
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

    private static void belongsToGroup(String scheduleGroupId, Schedule schedule) { //Todo 스케줄 안으로 리팩토링
        if (!schedule.getScheduleGroupId().equals(scheduleGroupId)) {
            throw new IllegalArgumentException("해당 스케줄의 그룹 아이디와 일치하지 않습니다."); //Todo 커스텀
        }
    }
}
