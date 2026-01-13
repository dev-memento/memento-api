package com.official.memento.orderinfo.service.usecase;

import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.service.command.ToDoPositionUpdateCommand;

public interface OrderInfoUpdateUseCase {

    void updatePosition(final ToDoPositionUpdateCommand toDoPositionUpdateCommand);

    OrderInfo updateOrderNum(OrderInfo orderInfo, double orderNum);

}
