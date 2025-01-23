package com.official.memento.orderinfo.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderInfoRepository {

    void save(final OrderInfo orderInfo);

    void update(final OrderInfo orderInfo);

    void deleteByScheduleId(final long scheduleId);

    void deleteByToDoId(final long toDoId);

    List<OrderWithScheduleOrToDo> findOrderInfoWithDetails(final LocalDate startDate);

    Integer findOrderByToDoId(final Long toDoId);

    OrderInfo findByToDoIdAndDate(final Long toDoId, final LocalDate date);

    OrderInfo updateOrderNum(final OrderInfo orderInfo , final int orderNum);

    List<OrderInfo> findOrdersBetween(LocalDate date, int startOrder, int endOrder);

    Optional<Integer> findOrderNumByToDoId(final Long toDoId);

    OrderInfo findByToDoId(Long toDoId);

    LocalDate findDateByToDoId(Long toDoId);
}
