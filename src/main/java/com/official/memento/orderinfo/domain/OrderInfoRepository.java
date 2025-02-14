package com.official.memento.orderinfo.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderInfoRepository {

    OrderInfo save(final OrderInfo orderInfo);

    void update(final OrderInfo orderInfo);

    void deleteByScheduleId(final long scheduleId);

    void deleteByToDoId(final long toDoId);

    List<OrderWithScheduleOrToDo> findOrderInfoWithDetails(final LocalDate startDate,final long memberId);

    Double findOrderByToDoId(final Long toDoId);

    OrderInfo findByToDoIdAndDate(final Long toDoId, final LocalDate date);

    OrderInfo updateOrderNum(final OrderInfo orderInfo , final double orderNum);

    List<OrderInfo> findOrdersBetween(LocalDate date, double startOrder, double endOrder);

    OrderInfo findByToDoId(Long toDoId);

    LocalDate findDateByToDoId(Long toDoId);
}
