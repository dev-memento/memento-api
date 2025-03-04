package com.official.memento.orderinfo.infrastructure.persistence;

import com.official.memento.orderinfo.domain.OrderInfo;

public class OrderInfoMapper {

    public static OrderInfo toDomain(final OrderInfoEntity entity) {
        return OrderInfo.withId(
            entity.getId(),
            entity.getMemberId(),
            entity.getScheduleId(),
            entity.getToDoId(),
            entity.getOrderNum(),
            entity.getDate(),
            entity.getPlanType(),
            entity.getCreatedAt()
        );
    }
}
