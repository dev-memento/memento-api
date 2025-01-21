package com.official.memento.orderinfo.infrastructure;

import com.official.memento.global.stereotype.Adapter;
import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo;
import com.official.memento.orderinfo.infrastructure.persistence.OrderInfoEntity;
import com.official.memento.orderinfo.infrastructure.persistence.OrderInfoEntityJpaRepository;
import com.official.memento.orderinfo.infrastructure.persistence.projection.OrderInfoProjection;

import java.time.LocalDate;
import java.util.List;

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
    public void deleteByToDoId(long toDoId){
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
                        projection.getEventType(),
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
}
