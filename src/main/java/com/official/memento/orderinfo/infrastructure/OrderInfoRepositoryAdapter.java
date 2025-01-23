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
import java.util.Optional;

@Adapter
public class OrderInfoRepositoryAdapter implements OrderInfoRepository {

    private final OrderInfoEntityJpaRepository orderInfoEntityJpaRepository;

    public OrderInfoRepositoryAdapter(OrderInfoEntityJpaRepository orderInfoEntityJpaRepository) {
        this.orderInfoEntityJpaRepository = orderInfoEntityJpaRepository;
    }

    @Override
    public void save(final OrderInfo orderInfo) {
        orderInfoEntityJpaRepository.save(OrderInfoEntity.of(orderInfo));
    }

    @Override
    public void update(final OrderInfo orderInfo) {
        orderInfoEntityJpaRepository.save(OrderInfoEntity.withId(orderInfo));
    }

    @Override
    public void deleteByScheduleId(long scheduleId) {
        orderInfoEntityJpaRepository.deleteByScheduleId(scheduleId);
    }

    @Override
    public void deleteByToDoId(long toDoId) {
        orderInfoEntityJpaRepository.deleteByToDoId(toDoId);
    }

    @Override
    public List<OrderWithScheduleOrToDo> findOrderInfoWithDetails(final LocalDate startDate) {
        List<OrderInfoProjection> projections = orderInfoEntityJpaRepository.findOrderInfoWithDetails(startDate);
        return projections.stream()
                .map(projection -> OrderWithScheduleOrToDo.of(
                        projection.getOrderInfoId(),
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
    public Integer findOrderByToDoId(Long toDoId) {
        return orderInfoEntityJpaRepository.findOrderByToDoId(toDoId)
                .map(orderInfo -> orderInfo.getOrderNum()) // 예: orderNum 필드 가져오기
                .orElse(null); // 결과가 없을 경우 null 반환
    }

    @Override
    public OrderInfo findByToDoIdAndDate(Long toDoId, LocalDate date) {
        return OrderInfoMapper.toDomain(orderInfoEntityJpaRepository.findByToDoIdAndDate(toDoId, date)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY)));
    }

    @Override
    public OrderInfo updateOrderNum(OrderInfo orderInfo, int orderNum) {
        OrderInfoEntity entity = orderInfoEntityJpaRepository.findById(orderInfo.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY));
        entity.updateOrderNum(orderNum);
        orderInfo.updateOrderNum(orderNum);
        return orderInfo;
    }
  
    public List<OrderInfo> findOrdersBetween(LocalDate date, int startOrder, int endOrder) {
        List<OrderInfoEntity> entities = orderInfoEntityJpaRepository.findOrdersBetween(date, startOrder, endOrder);
        return entities.stream()
                .map(entity -> OrderInfo.withId(
                        entity.getId(),
                        entity.getScheduleId(),
                        entity.getToDoId(),
                        entity.getOrderNum(),
                        entity.getDate(),
                        entity.getPlanType(),
                        entity.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public Optional<Integer> findOrderNumByToDoId(final Long toDoId) {
        return orderInfoEntityJpaRepository.findOrderNumByToDoId(toDoId);
    }

    @Override
    public OrderInfo findByToDoId(Long toDoId) {
        OrderInfoEntity orderInfoEntity = orderInfoEntityJpaRepository.findOrderByToDoId(toDoId)
                .orElseThrow(
                        () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY)
                );
        return OrderInfo.withId(
                orderInfoEntity.getId(),
                orderInfoEntity.getScheduleId(),
                orderInfoEntity.getToDoId(),
                orderInfoEntity.getOrderNum(),
                orderInfoEntity.getDate(),
                orderInfoEntity.getPlanType(),
                orderInfoEntity.getCreatedAt()
        );
    }

    @Override
    public LocalDate findDateByToDoId(Long toDoId) {
        return orderInfoEntityJpaRepository.findDateByToDoId(toDoId);
    }
}
