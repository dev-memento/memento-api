package com.official.memento.schedule.service;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.member.domain.port.MemberPersonalInfoRepository;
import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo;
import com.official.memento.orderinfo.domain.PlanType;
import com.official.memento.schedule.domain.ScheduleRepository;
import com.official.memento.schedule.domain.ScheduleTagRepository;
import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.entity.ScheduleTag;
import com.official.memento.schedule.service.command.*;
import com.official.memento.schedule.service.usecase.*;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.tag.domain.enums.TagColor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.official.memento.schedule.domain.enums.ScheduleType.APPLE;
import static com.official.memento.schedule.domain.enums.ScheduleType.NORMAL;

@Service
public class ScheduleService implements
        ScheduleCreateUseCase,
        RepeatScheduleCreateUseCase,
        ScheduleDeleteUseCase,
        ScheduleDeleteGroupUseCase,
        ScheduleUpdateUseCase,
        ScheduleUpdateGroupUseCase,
        ScheduleGetUseCase {

    private final ScheduleTagRepository scheduleTagRepository;
    private final ScheduleRepository scheduleRepository;
    private final TagRepository tagRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final MemberPersonalInfoRepository memberPersonalInfoRepository;

    public ScheduleService(
            final ScheduleRepository scheduleRepository,
            final ScheduleTagRepository scheduleTagRepository,
            final TagRepository tagRepository,
            final OrderInfoRepository orderInfoRepository,
            MemberPersonalInfoRepository memberPersonalInfoRepository) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleTagRepository = scheduleTagRepository;
        this.tagRepository = tagRepository;
        this.orderInfoRepository = orderInfoRepository;
        this.memberPersonalInfoRepository = memberPersonalInfoRepository;
    }

    @Override
    @Transactional
    public void create(final ScheduleCreateCommand command) {
        String scheduleGroupId = UUID.randomUUID().toString();
        Schedule schedule = createSchedule(command, scheduleGroupId);
        connectTag(command.tagId(), schedule);
        assignOrder(command.startDate().toLocalDate(), schedule, command.memberId());
    }

    @Override
    public void createAppleSchedules(final List<ScheduleCreateCommand> command) {
        Tag tag = tagRepository.findByMemberIdAndTagColor(command.get(0).memberId(), TagColor.GRAY05);
        for (ScheduleCreateCommand scheduleCreateCommand : command) {
            String scheduleGroupId = UUID.randomUUID().toString();
            Schedule schedule = createAppleSchedule(scheduleCreateCommand, scheduleGroupId);
            connectTag(tag.getId(), schedule);
            assignOrder(scheduleCreateCommand.startDate().toLocalDate(), schedule,command.get(0).memberId());
        }
    }

    @Override
    @Transactional
    public void update(final ScheduleUpdateCommand command) {
        Schedule schedule = scheduleRepository.findById(command.scheduleId());
        checkOwn(command.memberId(), schedule);
        schedule.update(
                command.description(),
                command.startDate(),
                command.endDate(),
                command.isAllDay()
        );
        scheduleRepository.update(schedule);
        updateOrDeleteTag(schedule, command.tagId());
        if (schedule.getStartDate() != command.startDate() || schedule.getEndDate() != command.endDate()) {
            orderInfoRepository.deleteByScheduleId(schedule.getId());
            assignOrder(command.startDate().toLocalDate(), schedule, command.memberId());

        }
    }

    @Override
    @Transactional
    public void delete(final ScheduleDeleteCommand command) {
        Schedule schedule = scheduleRepository.findById(command.scheduleId());
        checkOwn(command.memberId(), schedule);
        scheduleRepository.deleteById(schedule.getId());
        scheduleTagRepository.deleteByScheduleId(schedule.getId());
        orderInfoRepository.deleteByScheduleId(schedule.getId());
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
                scheduleGroupId
        );
        if (command.tagId() != null) {
            connectTags(command.tagId(), schedules);
        }
        //Todo 순서관련 로직 추가
    }

    @Override
    @Transactional
    public void deleteGroup(final ScheduleDeleteGroupCommand command) {
        Schedule schedule = scheduleRepository.findById(command.scheduleId());
        checkOwn(command.memberId(), schedule);
        belongsToGroup(command.scheduleGroupId(), schedule);
        List<Schedule> targetSchedules = scheduleRepository.findAllByScheduleGroupIdAndStartDateGreaterThanEqual(command.scheduleGroupId(), schedule.getStartDate());
        targetSchedules.forEach(targetSchedule -> removeTagConnection(targetSchedule.getId()));
        scheduleRepository.deleteAll(targetSchedules);
        //Todo 순서 관련 삭제
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
        oldSchedules.forEach(oldSchedule -> removeTagConnection(oldSchedule.getId()));
        List<Schedule> newSchedules = createRepeatSchedules(
                command.memberId(),
                command.description(),
                command.startDate(),
                command.endDate(),
                command.repeatOption(),
                command.repeatExpiredDate(),
                command.isAllDay(),
                schedule.getScheduleGroupId()
        );
        if (command.tagId() != null) {
            connectTags(command.tagId(), newSchedules);
        }
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
        ScheduleTag scheduleTag = scheduleTagRepository.findByScheduleId(scheduleId);
        schedule.setTagId(scheduleTag.getTagId());
        return schedule;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getSchedules(final long memberId, final LocalDate date) {
        return scheduleRepository.findAllByStartDateAndMemberId(date, memberId);
    }

    private Schedule createSchedule(final ScheduleCreateCommand command, final String scheduleGroupId) {
        return scheduleRepository.save(Schedule.of(
                command.memberId(),
                command.description(),
                command.startDate(),
                command.endDate(),
                command.isAllDay(),
                RepeatOption.NONE,
                null,
                NORMAL,
                scheduleGroupId
        ));
    }

    private Schedule createAppleSchedule(final ScheduleCreateCommand command, final String scheduleGroupId) {
        return scheduleRepository.save(Schedule.of(
                command.memberId(),
                command.description(),
                command.startDate(),
                command.endDate(),
                command.isAllDay(),
                RepeatOption.NONE,
                null,
                APPLE,
                scheduleGroupId
        ));
    }

    private void assignOrder(final LocalDate date,final Schedule schedule,final long memberId) {
        List<OrderWithScheduleOrToDo> scheduleList = orderInfoRepository.findOrderInfoWithDetails(date,memberId);
        double insertOrder = getInsertOrder(date, scheduleList, schedule);
        createOrderInfo(date, schedule, insertOrder,memberId);
    }

    private List<Schedule> createRepeatSchedules(
            final long memberId,
            final String description,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final boolean isAllDay,
            final String scheduleGroupId
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
                    scheduleGroupId
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
                default ->
                        throw new IllegalArgumentException("Unsupported repeat option: " + repeatOption);//Todo 커스텀 익셉션으로 교체 예정
            }
        }
        return schedules;
    }

    private void updateOrDeleteTag(final Schedule schedule, final long tagId) {
        ScheduleTag scheduleTag = scheduleTagRepository.findByScheduleId(schedule.getId());
       if (scheduleTag == null) {
            scheduleTag = ScheduleTag.of(tagId, schedule.getId());
            scheduleTagRepository.save(scheduleTag);
        } else if (scheduleTag.getTagId() != tagId) {
            scheduleTag.updateTag(tagId, scheduleTag.getUpdatedAt());
            scheduleTagRepository.update(scheduleTag);
        }
    }

    private void saveOrUpdateTag(ScheduleTag scheduleTag, final long scheduleId, final long tagId) {
        if (scheduleTag == null) {
            scheduleTag = ScheduleTag.of(tagId, scheduleId);
            scheduleTagRepository.save(scheduleTag);
        } else {
            scheduleTag.updateTag(tagId, scheduleTag.getUpdatedAt());
            scheduleTagRepository.save(scheduleTag);
        }

    }

    private void connectTags(final Long tagId, final List<Schedule> schedules) {
        schedules.forEach(schedule -> connectTag(tagId, schedule));
    }

    private void connectTag(final Long tagId, final Schedule schedule) {
        tagRepository.findById(tagId);
        ScheduleTag scheduleTag = ScheduleTag.of(tagId, schedule.getId());
        scheduleTagRepository.save(scheduleTag);
    }

    private void removeTagConnection(final long scheduleId) {
        scheduleTagRepository.deleteByScheduleId(scheduleId);
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

    private double getInsertOrder(final LocalDate date, final List<OrderWithScheduleOrToDo> scheduleList, final Schedule schedule) {
        double insertOrder = 1;
        boolean isInserted = false;
        for (OrderWithScheduleOrToDo existingOrder : scheduleList) {

            //순서정보중 스케줄의 시간을 고려하여 삽입해야할 위치 선정
            if (!isInserted && existingOrder.getType() == PlanType.SCHEDULE) {
                if (schedule.getStartDate().equals(existingOrder.getStartDate()) && schedule.getEndDate().isBefore(existingOrder.getEndDate())) {
                    insertOrder = existingOrder.getOrder();
                    isInserted = true;
                } else if (schedule.getStartDate().isBefore(existingOrder.getStartDate())) {
                    insertOrder = existingOrder.getOrder();
                    isInserted = true;
                }
            }

            //삽입 된 곳 이후의 순서정보들을 1씩 증가
            if (isInserted) {
                existingOrder.shiftBack();
                orderInfoRepository.update(
                        OrderInfo.withId(
                                existingOrder.getOrderInfoId(),
                                existingOrder.getMemberId(),
                                existingOrder.getScheduleId(),
                                existingOrder.getToDoId(),
                                existingOrder.getOrder(),
                                date,
                                existingOrder.getType(),
                                existingOrder.getCreatedAt()
                        ));
            }
        }

        //위 로직에 걸리지 않았을 경우 -> 빈리스트거나 가장 마지막에 추가되는 순서정보
        if (!isInserted) {
            insertOrder = scheduleList.isEmpty() ? 1 : scheduleList.get(scheduleList.size() - 1).getOrder() + 1;
        }
        return insertOrder;
    }

    private void createOrderInfo(final LocalDate date, final Schedule schedule, final double insertOrder,final long memberId) {
        orderInfoRepository.save(OrderInfo.of(
                memberId,
                schedule.getId(),
                null,
                insertOrder,
                date,
                PlanType.SCHEDULE,
                LocalDateTime.now()
        ));
    }
}
