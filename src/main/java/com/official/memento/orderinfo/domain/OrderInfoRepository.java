package com.official.memento.orderinfo.domain;

import java.time.LocalDate;
import java.util.List;

public interface OrderInfoRepository {

    OrderInfo save(final OrderInfo orderInfo);

    void update(final OrderInfo orderInfo);

    void deleteByScheduleId(final long scheduleId);

    void deleteByToDoId(final long toDoId);

    List<OrderWithScheduleOrToDo> findOrderInfoWithDetails(final LocalDate startDate, final long memberId);

    OrderInfo findByToDoIdAndDate(final Long toDoId, final LocalDate date);

    OrderInfo updateOrderNum(final OrderInfo orderInfo, final double orderNum);

    OrderInfo findByToDoId(Long toDoId);

    OrderInfo findByScheduleId(Long scheduleId);

    List<OrderInfo> findAllByMemberIdAndDateOrderByOrderNum(final long memberId, final LocalDate date);

    /**
     * 배치 업데이트: 여러 OrderInfo의 orderNum을 한 번에 업데이트
     * Dirty Checking 기반으로 트랜잭션 커밋 시 일괄 반영
     */
    void updateAllOrderNums(final List<OrderInfo> orderInfoList);
}
