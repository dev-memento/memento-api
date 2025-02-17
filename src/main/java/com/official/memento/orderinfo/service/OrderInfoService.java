package com.official.memento.orderinfo.service;

import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
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
    public void assignToDoOrder(final LocalDate date, final ToDo toDo, final long memberId) {
        List<OrderInfo> orderInfoList = orderInfoRepository.findAllByMemberIdAndDateOrderByOrderNum(memberId, date);
        double insertOrder = orderInfoList.get(0).getOrderNum() / 2;
        createToDoOrderInfo(date, toDo, insertOrder, memberId);
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

    @Override
    public void deleteByToDoId(final long toDoId) {
        orderInfoRepository.deleteByToDoId(toDoId);
    }

    @Override
    public OrderInfo findByToDoId(final long toDoId) {
        return orderInfoRepository.findByToDoId(toDoId);
    }
}
