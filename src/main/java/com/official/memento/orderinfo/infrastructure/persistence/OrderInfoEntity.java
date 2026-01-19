package com.official.memento.orderinfo.infrastructure.persistence;

import com.official.memento.orderinfo.domain.PlanType;
import com.official.memento.orderinfo.domain.OrderInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "order_info")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class OrderInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long memberId;
    private Long scheduleId;
    private Long toDoId;
    private double orderNum;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private PlanType planType;
    private LocalDateTime createdAt;

    private OrderInfoEntity(
            final long memberId,
            final Long scheduleId,
            final Long toDoId,
            final double orderNum,
            final LocalDate date,
            final PlanType planType,
            final LocalDateTime createdAt
    ) {
        this.memberId = memberId;
        this.scheduleId = scheduleId;
        this.toDoId = toDoId;
        this.orderNum = orderNum;
        this.date = date;
        this.planType = planType;
        this.createdAt = createdAt;
    }

    public static OrderInfoEntity of(final OrderInfo orderInfo) {
        return new OrderInfoEntity(
                orderInfo.getMemberId(),
                orderInfo.getScheduleId(),
                orderInfo.getToDoId(),
                orderInfo.getOrderNum(),
                orderInfo.getDate(),
                orderInfo.getPlanType(),
                orderInfo.getCreatedAt()
        );
    }

    public static OrderInfoEntity withId(
            final OrderInfo orderInfo
    ) {
        return new OrderInfoEntity(
                orderInfo.getId(),
                orderInfo.getMemberId(),
                orderInfo.getScheduleId(),
                orderInfo.getToDoId(),
                orderInfo.getOrderNum(),
                orderInfo.getDate(),
                orderInfo.getPlanType(),
                orderInfo.getCreatedAt()
        );
    }

    public void updateOrderNum(final double orderNum) {
        this.orderNum = orderNum;
    }
}
