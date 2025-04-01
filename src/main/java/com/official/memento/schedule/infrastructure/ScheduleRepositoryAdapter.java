package com.official.memento.schedule.infrastructure;

import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.stereotype.Adapter;
import com.official.memento.schedule.domain.ScheduleRepository;
import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.enums.ScheduleType;
import com.official.memento.schedule.infrastructure.persistence.ScheduleEntity;
import com.official.memento.schedule.infrastructure.persistence.ScheduleEntityJpaRepository;
import com.official.memento.schedule.infrastructure.persistence.projection.ScheduleOrderInfoProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class ScheduleRepositoryAdapter implements ScheduleRepository {

    private final ScheduleEntityJpaRepository scheduleEntityJpaRepository;

    @Override
    public Schedule save(final Schedule schedule) {
        ScheduleEntity scheduleEntity = scheduleEntityJpaRepository.save(ScheduleEntity.of(schedule));
        return Schedule.withId(
                scheduleEntity.getId(),
                scheduleEntity.getMemberId(),
                scheduleEntity.getDescription(),
                scheduleEntity.getStartDate(),
                scheduleEntity.getEndDate(),
                scheduleEntity.isAllDay(),
                scheduleEntity.getRepeatOption(),
                scheduleEntity.getRepeatExpiredDate(),
                scheduleEntity.getType(),
                scheduleEntity.getScheduleGroupId(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getTagId()
        );
    }

    @Override
    public Schedule update(final Schedule schedule) {
        ScheduleEntity scheduleEntity = scheduleEntityJpaRepository.save(ScheduleEntity.from(schedule));
        return Schedule.withId(
                scheduleEntity.getId(),
                scheduleEntity.getMemberId(),
                scheduleEntity.getDescription(),
                scheduleEntity.getStartDate(),
                scheduleEntity.getEndDate(),
                scheduleEntity.isAllDay(),
                scheduleEntity.getRepeatOption(),
                scheduleEntity.getRepeatExpiredDate(),
                scheduleEntity.getType(),
                scheduleEntity.getScheduleGroupId(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getTagId()
        );
    }

    @Override
    public Schedule findById(long scheduleId) {
        ScheduleEntity scheduleEntity = scheduleEntityJpaRepository.findById(scheduleId)
                .orElseThrow(
                        () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY)
                );
        return Schedule.withId(
                scheduleEntity.getId(),
                scheduleEntity.getMemberId(),
                scheduleEntity.getDescription(),
                scheduleEntity.getStartDate(),
                scheduleEntity.getEndDate(),
                scheduleEntity.isAllDay(),
                scheduleEntity.getRepeatOption(),
                scheduleEntity.getRepeatExpiredDate(),
                scheduleEntity.getType(),
                scheduleEntity.getScheduleGroupId(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getTagId()
        );
    }


    @Override
    public List<Schedule> findNonAllDaySchedulesWithOrderInfo(long memberId) {
        List<ScheduleOrderInfoProjection> schedulesWithOrderInfo = scheduleEntityJpaRepository
                .findNonAllDaySchedulesWithOrderInfo(memberId);
        return schedulesWithOrderInfo.stream().map(scheduleEntity -> Schedule.withIdAndOrderAndTag(
                scheduleEntity.getScheduleId(),
                scheduleEntity.getMemberId(),
                scheduleEntity.getDescription(),
                scheduleEntity.getStartDate(),
                scheduleEntity.getEndDate(),
                scheduleEntity.getIsAllDay(),
                scheduleEntity.getRepeatOption(),
                scheduleEntity.getRepeatExpiredDate(),
                scheduleEntity.getType(),
                scheduleEntity.getScheduleGroupId(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getOrderNum(),
                scheduleEntity.getTagName(),
                scheduleEntity.getTagColor(),
                scheduleEntity.getTagId()
        )).toList();
    }

    @Override
    public List<Schedule> findAllAlDaysByMemberId(long memberId) {
        List<ScheduleOrderInfoProjection> scheduleEntities = scheduleEntityJpaRepository.findAllDaySchedulesByMemberIdOrderedByStartDate(
                memberId);
        return scheduleEntities.stream().map(scheduleEntity -> Schedule.withIdAndTag(
                scheduleEntity.getScheduleId(),
                scheduleEntity.getMemberId(),
                scheduleEntity.getDescription(),
                scheduleEntity.getStartDate(),
                scheduleEntity.getEndDate(),
                scheduleEntity.getIsAllDay(),
                scheduleEntity.getRepeatOption(),
                scheduleEntity.getRepeatExpiredDate(),
                scheduleEntity.getType(),
                scheduleEntity.getScheduleGroupId(),
                scheduleEntity.getEndDate(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getTagName(),
                scheduleEntity.getTagColor(),
                scheduleEntity.getTagId()
        )).toList();
    }

    @Override
    public List<Schedule> findAllByStartDateAndMemberId(final LocalDate startDate, final long memberId) {
        List<ScheduleOrderInfoProjection> schedules = scheduleEntityJpaRepository.findSchedulesByMemberIdAndDateOrderedByOrderNum(
                memberId, startDate);
        return schedules.stream().map(scheduleEntity -> Schedule.withIdAndOrderAndTag(
                scheduleEntity.getScheduleId(),
                scheduleEntity.getMemberId(),
                scheduleEntity.getDescription(),
                scheduleEntity.getStartDate(),
                scheduleEntity.getEndDate(),
                scheduleEntity.getIsAllDay(),
                scheduleEntity.getRepeatOption(),
                scheduleEntity.getRepeatExpiredDate(),
                scheduleEntity.getType(),
                scheduleEntity.getScheduleGroupId(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getOrderNum(),
                scheduleEntity.getTagName(),
                scheduleEntity.getTagColor(),
                scheduleEntity.getTagId()
        )).toList();
    }

    @Override
    public List<Schedule> findAllAppleByMemberId(final long memberId) {
        List<ScheduleEntity> scheduleEntities = scheduleEntityJpaRepository.findAllByMemberIdAndType(memberId,
                ScheduleType.APPLE);
        return scheduleEntities.stream().map(scheduleEntity -> Schedule.withId(
                scheduleEntity.getId(),
                scheduleEntity.getMemberId(),
                scheduleEntity.getDescription(),
                scheduleEntity.getStartDate(),
                scheduleEntity.getEndDate(),
                scheduleEntity.isAllDay(),
                scheduleEntity.getRepeatOption(),
                scheduleEntity.getRepeatExpiredDate(),
                scheduleEntity.getType(),
                scheduleEntity.getScheduleGroupId(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getTagId()
        )).toList();
    }

    @Override
    public Optional<Schedule> findByScheduleGroupIdOrNull(final String scheduleGroupId) {
        Optional<ScheduleEntity> scheduleEntity = scheduleEntityJpaRepository.findByScheduleGroupId(scheduleGroupId);
        return scheduleEntity.map(entity -> Schedule.withId(
                entity.getId(),
                entity.getMemberId(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isAllDay(),
                entity.getRepeatOption(),
                entity.getRepeatExpiredDate(),
                entity.getType(),
                entity.getScheduleGroupId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getTagId()
        ));
    }

    @Override
    public List<Schedule> findAllByScheduleGroupIdAndStartDateGreaterThanEqual(String scheduleGroupId,
                                                                               LocalDateTime startDate) {
        List<ScheduleEntity> scheduleEntities = scheduleEntityJpaRepository.findAllByScheduleGroupIdAndStartDateGreaterThanEqual(
                scheduleGroupId, startDate);
        return scheduleEntities.stream().map(scheduleEntity -> Schedule.withId(
                scheduleEntity.getId(),
                scheduleEntity.getMemberId(),
                scheduleEntity.getDescription(),
                scheduleEntity.getStartDate(),
                scheduleEntity.getEndDate(),
                scheduleEntity.isAllDay(),
                scheduleEntity.getRepeatOption(),
                scheduleEntity.getRepeatExpiredDate(),
                scheduleEntity.getType(),
                scheduleEntity.getScheduleGroupId(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getTagId()
        )).toList();
    }

    @Override
    public List<Schedule> findByStartDateAfterAndEndDateLessThanEqual(final LocalDateTime startTime, final LocalDateTime endTime) {
        List<ScheduleEntity> scheduleEntities = scheduleEntityJpaRepository.findByStartDateAfterAndEndDateLessThanEqual(startTime, endTime);
        return scheduleEntities.stream().map(scheduleEntity -> Schedule.withId(
                scheduleEntity.getId(),
                scheduleEntity.getMemberId(),
                scheduleEntity.getDescription(),
                scheduleEntity.getStartDate(),
                scheduleEntity.getEndDate(),
                scheduleEntity.isAllDay(),
                scheduleEntity.getRepeatOption(),
                scheduleEntity.getRepeatExpiredDate(),
                scheduleEntity.getType(),
                scheduleEntity.getScheduleGroupId(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getTagId()
        )).toList();
    }

    @Override
    public void updateTagForSchedules(final long oldTagId, final long newTagId){
        scheduleEntityJpaRepository.updateTagForSchedules(oldTagId, newTagId);
    }

    @Override
    public void deleteById(final long scheduleId) {
        scheduleEntityJpaRepository.deleteById(scheduleId);
    }

    @Override
    public void deleteAll(final List<Schedule> schedules) {
        List<ScheduleEntity> scheduleEntities = schedules.stream().map(ScheduleEntity::from).toList();
        scheduleEntityJpaRepository.deleteAll(scheduleEntities);
    }

    @Override
    public void deleteAllByScheduleGroupId(final String scheduleGroupId) {
        scheduleEntityJpaRepository.deleteAllByScheduleGroupId(scheduleGroupId);
    }

}
