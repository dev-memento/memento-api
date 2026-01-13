package com.official.memento.orderinfo.service;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.global.lock.DistributedLock;
import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo;
import com.official.memento.orderinfo.domain.PlanType;
import com.official.memento.orderinfo.service.command.ToDoPositionUpdateCommand;
import com.official.memento.orderinfo.service.usecase.OrderInfoCreateUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoDeleteUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoGetUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoUpdateUseCase;
import com.official.memento.schedule.domain.ScheduleRepository;
import com.official.memento.schedule.domain.entity.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    @DistributedLock(key = "'order-info:' + #command.memberId() + ':' + #command.date()")
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
        insertOrder = checkReOrdering(command.date(), command.memberId(), insertOrder);
        OrderInfo updated = selectedTodoOrderInfo.toBuilder()
                .orderNum(insertOrder)
                .build();
        orderInfoRepository.update(updated);
    }

    @Override
    @Transactional
    @DistributedLock(key = "'order-info:' + #memberId + ':' + #date")
    public void assignToDoOrder(final LocalDate date, final long toDoId, final long memberId) {
        List<OrderInfo> orderInfoList = orderInfoRepository.findAllByMemberIdAndDateOrderByOrderNum(memberId, date);
        double preOrder = 0;
        double nextOrder = orderInfoList.isEmpty() ? 1 : orderInfoList.get(0).getOrderNum();
        double insertOrder = (preOrder + nextOrder) / 2;
        insertOrder = checkReOrdering(date, memberId, insertOrder);
        createToDoOrderInfo(date, toDoId, insertOrder, memberId);
    }

    @Override
    @Transactional
    @DistributedLock(key = "'order-info:' + #memberId + ':' + #date")
    public void assignScheduleOrder(final LocalDate date, final long scheduleId, final long memberId){
        Schedule schedule = scheduleRepository.findById(scheduleId);
        List<OrderWithScheduleOrToDo> orderInfoList = orderInfoRepository.findOrderInfoWithDetails(date,memberId);
        double insertOrder = 1;
        boolean isInserted = false;

        for (int i = 0; i < orderInfoList.size(); i++) {
            OrderWithScheduleOrToDo existingOrder = orderInfoList.get(i);
            if (existingOrder.getType() == PlanType.SCHEDULE) {
                if (schedule.getStartDate().equals(existingOrder.getStartDate())) {
                    if (schedule.getEndDate().isBefore(existingOrder.getEndDate())) {
                        double previousOrder = (i > 0) ? orderInfoList.get(i - 1).getOrder() : 0;
                        insertOrder = (previousOrder + existingOrder.getOrder()) / 2;
                        isInserted = true;
                        break;
                    }
                } else if (schedule.getStartDate().isBefore(existingOrder.getStartDate())) {
                    double previousOrder = (i > 0) ? orderInfoList.get(i - 1).getOrder() : 0;
                    insertOrder = (previousOrder + existingOrder.getOrder()) / 2;
                    isInserted = true;
                    break;
                }
            }
        }

        if (!isInserted) {
            insertOrder = orderInfoList.isEmpty() ? 1 : orderInfoList.get(orderInfoList.size() - 1).getOrder() + 1;
        }

        insertOrder = checkReOrdering(date, memberId, insertOrder);
        createScheduleOrderInfo(date, scheduleId, insertOrder, memberId);
    }

    /**
     * 부동소수점 정밀도 한계 감지 시 자동 재정렬
     * - 임계치: insertOrder < 1e-10
     * - 재정렬 시 모든 아이템의 순서를 1, 2, 3... 으로 초기화하여 정밀도 확보
     * - 배치 업데이트로 N+1 쿼리 문제 해결
     */
    private double checkReOrdering(final LocalDate date, final long memberId, final double insertOrder) {
        if (insertOrder < 1e-10) {
            List<OrderInfo> afterOrderInfoList = orderInfoRepository.findAllByMemberIdAndDateOrderByOrderNum(memberId,
                    date);

            // 메모리에서 재정렬 계산
            List<OrderInfo> reorderedList = new ArrayList<>();
            for (int i = 0; i < afterOrderInfoList.size(); i++) {
                OrderInfo reordered = afterOrderInfoList.get(i).toBuilder()
                        .orderNum(i + 1)
                        .build();
                reorderedList.add(reordered);
            }

            // 배치 업데이트로 한 번에 반영 (Dirty Checking 기반)
            orderInfoRepository.updateAllOrderNums(reorderedList);
            return 1;
        }
        return insertOrder;
    }

    private void createToDoOrderInfo(final LocalDate date, final long toDoId, final double insertOrder,
                                     final long memberId) {
        orderInfoRepository.save(OrderInfo.of(
                memberId,
                null,
                toDoId,
                insertOrder,
                date,
                PlanType.TODO,
                LocalDateTime.now()
        ));
    }

    private void createScheduleOrderInfo(final LocalDate date, final long scheduleId, final double insertOrder,
                                     final long memberId) {
        orderInfoRepository.save(OrderInfo.of(
                memberId,
                scheduleId,
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

    @Override
    public OrderInfo findByToDoIdAndDate(Long toDoId, LocalDate date) {
        return orderInfoRepository.findByToDoIdAndDate(toDoId, date);
    }

    @Override
    @Transactional
    public OrderInfo updateOrderNum(OrderInfo orderInfo, double orderNum) {
        return orderInfoRepository.updateOrderNum(orderInfo, orderNum);
    }

    private static void checkInValidRequest(final double previousOrder, final double insertOrder) {
        if (previousOrder > insertOrder) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_JSON_FORMAT);
        }
    }

}
