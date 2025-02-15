package com.official.memento.orderinfo.service.usecase;

import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo;
import java.time.LocalDate;
import java.util.List;

public interface OrderInfoGetUseCase {
    List<OrderWithScheduleOrToDo> findOrderInfoWithDetails(final LocalDate startDate, final long memberId);
    OrderInfo findByToDoId(final long toDoId);
}
