package com.official.memento.orderinfo.service.usecase;

import com.official.memento.orderinfo.domain.OrderInfo;

public interface OrderInfoGetUseCase {
    OrderInfo findByToDoId(final long toDoId);
}
