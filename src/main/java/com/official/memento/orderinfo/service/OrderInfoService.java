package com.official.memento.orderinfo.service;

import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo;
import com.official.memento.orderinfo.domain.PlanType;
import com.official.memento.todo.domain.ToDo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderInfoService {

    private final OrderInfoRepository orderInfoRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void assignOrder(final LocalDate date, final ToDo toDo, final long memberId) {
        List<OrderWithScheduleOrToDo> toDoList = orderInfoRepository.findOrderInfoWithDetails(date, memberId);
        double insertOrder = getInsertOrder(date, toDoList, toDo);
        OrderInfo createdOrderInfo = createOrderInfo(date, toDo, insertOrder, memberId);
        toDo.updateOrderNum(createdOrderInfo.getOrderNum());
    }

    private double getInsertOrder(final LocalDate date, final List<OrderWithScheduleOrToDo> toDoList, final ToDo toDo) {
        double insertOrder = 1;
        boolean isInserted = false;
        for (OrderWithScheduleOrToDo existingOrder : toDoList) {

            if (!isInserted && existingOrder.getType() == PlanType.TODO) {
                if (toDo.getPriorityValue() > existingOrder.getPriorityValue()) {
                    insertOrder = existingOrder.getOrder();
                    isInserted = true;
                    break;
                }
                if (toDo.getPriorityValue().equals(existingOrder.getPriorityValue())) {
                    if (toDo.getCreatedAt().isBefore(existingOrder.getCreatedAt())) {
                        insertOrder = existingOrder.getOrder();
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
}
