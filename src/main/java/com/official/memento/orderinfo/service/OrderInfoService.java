package com.official.memento.orderinfo.service;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo;
import com.official.memento.orderinfo.domain.PlanType;
import com.official.memento.orderinfo.infrastructure.persistence.OrderInfoEntity;
import com.official.memento.orderinfo.service.command.ToDoPositionUpdateCommand;
import com.official.memento.orderinfo.service.usecase.OrderInfoCreateUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoDeleteUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoGetUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoUpdateUseCase;
import com.official.memento.schedule.domain.entity.Schedule;
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
        OrderInfoGetUseCase {

    private final OrderInfoRepository orderInfoRepository;

    @Override
    @Transactional
    public void updatePosition(final ToDoPositionUpdateCommand command) {
        OrderInfo selectedTodoOrderInfo = orderInfoRepository.findByToDoId(command.toDoId());

        //이전 투두의 id가 null이면 목표 위치가 첫번째
        double previousOrder = command.previousToDoId() == null ? 0
                : orderInfoRepository.findByToDoId(command.previousToDoId()).getOrderNum();

        //다음 투두의 id가 null이면 목표 위치가 마지막
        double insertOrder;
        if (command.nextToDoId() == null) {
            insertOrder = previousOrder + 1;
        } else {
            insertOrder = (previousOrder + orderInfoRepository.findByToDoId(command.nextToDoId()).getOrderNum()) / 2;
        }
        checkInValidRequest(previousOrder, insertOrder);
        selectedTodoOrderInfo.updateOrderNum(insertOrder);
        orderInfoRepository.update(selectedTodoOrderInfo);
        checkReOrdering(selectedTodoOrderInfo.getDate(), command.memberId(), insertOrder);
    }

    @Override
    @Transactional
    public void assignToDoOrder(final LocalDate date, final ToDo toDo, final long memberId) {
        List<OrderInfo> orderInfoList = orderInfoRepository.findAllByMemberIdAndDateOrderByOrderNum(memberId, date);
        double preOrder = 0;
        double nextOrder = orderInfoList.isEmpty() ? 1 : orderInfoList.get(0).getOrderNum();
        double insertOrder = (preOrder + nextOrder) / 2;
        boolean checked = checkReOrdering(date, memberId, insertOrder);
        createToDoOrderInfo(date, toDo, checked ? 1 : insertOrder, memberId);
    }

    @Override
    @Transactional
    public void assignScheduleOrder(final LocalDate date, final Schedule schedule, final long memberId) {
        List<OrderWithScheduleOrToDo> orderInfoList = orderInfoRepository.findOrderInfoWithDetails(date, memberId);
        double insertOrder = -1;
        for (int i = 0; i < orderInfoList.size(); i++) {
            OrderWithScheduleOrToDo existingOrder = orderInfoList.get(i);
            if (existingOrder.getType() == PlanType.SCHEDULE) {
                if (schedule.getStartDate().equals(existingOrder.getStartDate())) {
                    if (schedule.getEndDate().isBefore(existingOrder.getEndDate())) {
                        double previousOrder = (i > 0) ? orderInfoList.get(i - 1).getOrder() : 0;
                        insertOrder = (previousOrder + existingOrder.getOrder()) / 2;
                        break;
                    }
                }
                else if (schedule.getStartDate().isBefore(existingOrder.getStartDate())) {
                    double previousOrder = (i > 0) ? orderInfoList.get(i - 1).getOrder() : 0;
                    insertOrder = (previousOrder + existingOrder.getOrder()) / 2;
                    break;
                }
            }
        }

        if (insertOrder == -1) {
            insertOrder = orderInfoList.isEmpty() ? 1 : orderInfoList.get(orderInfoList.size() - 1).getOrder() + 1;
        }

        boolean checked = checkReOrdering(date, memberId, insertOrder);
        createScheduleOrderInfo(date, schedule, checked ? 1 : insertOrder, memberId);
    }

    private boolean checkReOrdering(final LocalDate date, final long memberId, final double insertOrder) {
        if (insertOrder < 1e-10) {
            List<OrderInfo> afterOrderInfoList = orderInfoRepository.findAllByMemberIdAndDateOrderByOrderNum(memberId,
                    date);
            for (int i = 0; i < afterOrderInfoList.size(); i++) {
                afterOrderInfoList.get(i).updateOrderNum(i + 1);
                orderInfoRepository.update(afterOrderInfoList.get(i));
            }
            return true;
        }
        return false;
    }

    private void createToDoOrderInfo(final LocalDate date, final ToDo toDo, final double insertOrder,
                                     final long memberId) {
        orderInfoRepository.save(OrderInfo.of(
                memberId,
                null,
                toDo.getId(),
                insertOrder,
                date,
                PlanType.TODO,
                LocalDateTime.now()
        ));
    }

    private void createScheduleOrderInfo(final LocalDate date, final Schedule schedule, final double insertOrder,
                                     final long memberId) {
        orderInfoRepository.save(OrderInfo.of(
                memberId,
                schedule.getId(),
                null,
                insertOrder,
                date,
                PlanType.SCHEDULE,
                LocalDateTime.now()
        ));
    }

    @Override
    public void deleteByToDoId(final long toDoId) {
        orderInfoRepository.deleteByToDoId(toDoId);
    }

    @Override
    public void deleteByScheduleId(final long scheduleId) {
        orderInfoRepository.deleteByScheduleId(scheduleId);
    }

    @Override
    public OrderInfo findByToDoId(final long toDoId) {
        return orderInfoRepository.findByToDoId(toDoId);
    }

    @Override
    public OrderInfo findByScheduleId(final long scheduleId) {return  orderInfoRepository.findByScheduleId(scheduleId);}

    private static void checkInValidRequest(final double previousOrder, final double insertOrder) {
        if (previousOrder > insertOrder) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_JSON_FORMAT);
        }
    }

}
