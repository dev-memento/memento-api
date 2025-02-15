package com.official.memento.orderinfo.service;

import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo;
import com.official.memento.orderinfo.domain.PlanType;
import com.official.memento.orderinfo.service.command.ToDoPositionUpdateCommand;
import com.official.memento.orderinfo.service.usecase.OrderInfoCreateUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoDeleteUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoGetUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoUpdateUseCase;
import com.official.memento.todo.domain.entity.ToDo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderInfoService implements
        OrderInfoUpdateUseCase,
        OrderInfoCreateUseCase,
        OrderInfoDeleteUseCase,
        OrderInfoGetUseCase
{

    private final OrderInfoRepository orderInfoRepository;

    @Override
    @Transactional
    public void updatePosition(final ToDoPositionUpdateCommand command) {
        OrderInfo selectedTodoOrderInfo = orderInfoRepository.findByToDoId(command.toDoId());
        double previousOrder = command.previousToDoId() == null ? 0
                : orderInfoRepository.findByToDoId(command.previousToDoId()).getOrderNum();
        if (command.nextToDoId() == null) {
            selectedTodoOrderInfo.updateOrderNum(previousOrder + 1);
        } else {
            double nextOrder = orderInfoRepository.findByToDoId(command.nextToDoId()).getOrderNum();
            selectedTodoOrderInfo.updateOrderNum((previousOrder + nextOrder) / 2);
        }
        orderInfoRepository.update(selectedTodoOrderInfo);
    }

    @Override
    @Transactional
    public void assignOrder(final LocalDate date, final ToDo toDo, final long memberId) {
        List<OrderWithScheduleOrToDo> toDoList = orderInfoRepository.findOrderInfoWithDetails(date, memberId);
        double insertOrder = getInsertOrder(toDoList, toDo);
        OrderInfo createdOrderInfo = createOrderInfo(date, toDo, insertOrder, memberId);
    }

    private double getInsertOrder(final List<OrderWithScheduleOrToDo> toDoList, final ToDo toDo) {
        double insertOrder = 1;
        boolean isInserted = false;

        for (int i = 0; i < toDoList.size(); i++) {
            OrderWithScheduleOrToDo existingOrder = toDoList.get(i);
            if (existingOrder.getType() == PlanType.TODO) {
                if (toDo.getPriorityValue() > existingOrder.getPriorityValue()) {
                    if (i + 1 < toDoList.size()) {
                        insertOrder = (existingOrder.getOrder() + toDoList.get(i + 1).getOrder()) / 2.0;
                    } else {
                        insertOrder = existingOrder.getOrder() + 1;
                    }
                    isInserted = true;
                    break;
                }
                if (toDo.getPriorityValue().equals(existingOrder.getPriorityValue())) {
                    if (toDo.getCreatedAt().isBefore(existingOrder.getCreatedAt())) {
                        if (i + 1 < toDoList.size()) {
                            insertOrder = (existingOrder.getOrder() + toDoList.get(i + 1).getOrder()) / 2.0;
                        } else {
                            insertOrder = existingOrder.getOrder() + 1;
                        }
                        isInserted = true;
                        break;
                    }
                }
            }
        }

        if (!isInserted) {
            insertOrder = toDoList.isEmpty() ? 1 : toDoList.get(toDoList.size() - 1).getOrder() + 1;
        }
        return insertOrder;
    }

    private OrderInfo createOrderInfo(final LocalDate date, final ToDo toDo, final double insertOrder,
                                      final long memberId) {
        return orderInfoRepository.save(OrderInfo.of(
                memberId,
                null,
                toDo.getId(),
                insertOrder,
                date,
                PlanType.TODO,
                LocalDateTime.now()
        ));
    }

    @Override
    public void deleteByToDoId(final long toDoId) {
        orderInfoRepository.deleteByToDoId(toDoId);
    }

    @Override
    public List<OrderWithScheduleOrToDo> findOrderInfoWithDetails(final LocalDate startDate, final long memberId) {
        return orderInfoRepository.findOrderInfoWithDetails(startDate, memberId);
    }
}
