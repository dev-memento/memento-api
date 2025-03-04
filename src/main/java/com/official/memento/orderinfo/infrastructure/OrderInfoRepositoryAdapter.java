package com.official.memento.orderinfo.infrastructure;

import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.stereotype.Adapter;
import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo;
import com.official.memento.orderinfo.infrastructure.persistence.OrderInfoEntity;
import com.official.memento.orderinfo.infrastructure.persistence.OrderInfoEntityJpaRepository;
import com.official.memento.orderinfo.infrastructure.persistence.OrderInfoMapper;
import com.official.memento.orderinfo.infrastructure.persistence.projection.OrderInfoProjection;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class OrderInfoRepositoryAdapter implements OrderInfoRepository {

    private final OrderInfoEntityJpaRepository orderInfoEntityJpaRepository;

    @Override
    public OrderInfo save(final OrderInfo orderInfo) {
        return OrderInfoMapper.toDomain(orderInfoEntityJpaRepository.save(OrderInfoEntity.of(orderInfo)));
    }

    @Override
    public void update(final OrderInfo orderInfo) {
        orderInfoEntityJpaRepository.findById(orderInfo.getId())
                .ifPresent(entity -> {
                    entity.updateOrderNum(orderInfo.getOrderNum());
                });
    }

    @Override
    public void deleteByScheduleId(final long scheduleId) {
        orderInfoEntityJpaRepository.deleteByScheduleId(scheduleId);
    }

    @Override
    public void deleteByToDoId(final long toDoId) {
        orderInfoEntityJpaRepository.deleteByToDoId(toDoId);
    }

    @Override
    public List<OrderInfo> findAllByMemberIdAndDateOrderByOrderNum(final long memberId, final LocalDate date) {
        List<OrderInfoEntity> entities = orderInfoEntityJpaRepository.findAllByMemberIdAndDateOrderByOrderNum(
                memberId,
                date);
        return entities.stream()
                .map(e -> OrderInfo.withId(
                        e.getId(),
                        e.getMemberId(),
                        e.getScheduleId(),
                        e.getToDoId(),
                        e.getOrderNum(),
                        e.getDate(),
                        e.getPlanType(),
                        e.getCreatedAt()
                )).toList();
    }

    @Override
    public List<OrderWithScheduleOrToDo> findOrderInfoWithDetails(final LocalDate startDate, final long memberId) {
        List<OrderInfoProjection> projections = orderInfoEntityJpaRepository.findOrderInfoWithDetails(startDate,
                memberId);
        return projections.stream()
                .map(projection -> OrderWithScheduleOrToDo.of(
                        projection.getOrderInfoId(),
                        projection.getMemberId(),
                        projection.getScheduleId(),
                        projection.getToDoId(),
                        projection.getStartDate(),
                        projection.getEndDate(),
                        projection.getPriorityValue(),
                        projection.getOrderNum(),
                        projection.getPlanType(),
                        projection.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public OrderInfo findByToDoIdAndDate(final Long toDoId, final LocalDate date) {
        return OrderInfoMapper.toDomain(orderInfoEntityJpaRepository.findByToDoIdAndDate(toDoId, date)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY)));
    }

    @Override
    public OrderInfo updateOrderNum(final OrderInfo orderInfo, final double orderNum) {
        OrderInfoEntity entity = orderInfoEntityJpaRepository.findById(orderInfo.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY));
        entity.updateOrderNum(orderNum);
        orderInfo.updateOrderNum(orderNum);
        return orderInfo;
    }

    @Override
    public OrderInfo findByToDoId(final Long toDoId) {
        OrderInfoEntity orderInfoEntity = orderInfoEntityJpaRepository.findByToDoId(toDoId)
                .orElseThrow(
                        () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY)
                );
        return OrderInfo.withId(
                orderInfoEntity.getId(),
                orderInfoEntity.getMemberId(),
                orderInfoEntity.getScheduleId(),
                orderInfoEntity.getToDoId(),
                orderInfoEntity.getOrderNum(),
                orderInfoEntity.getDate(),
                orderInfoEntity.getPlanType(),
                orderInfoEntity.getCreatedAt()
        );
    }

    @Override
    public OrderInfo findByScheduleId(final Long scheduleId) {
        OrderInfoEntity orderInfoEntity = orderInfoEntityJpaRepository.findByScheduleId(scheduleId)
                .orElseThrow(
                        () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY)
                );
        return OrderInfo.withId(
                orderInfoEntity.getId(),
                orderInfoEntity.getMemberId(),
                orderInfoEntity.getScheduleId(),
                orderInfoEntity.getToDoId(),
                orderInfoEntity.getOrderNum(),
                orderInfoEntity.getDate(),
                orderInfoEntity.getPlanType(),
                orderInfoEntity.getCreatedAt()
        );
    }
}
