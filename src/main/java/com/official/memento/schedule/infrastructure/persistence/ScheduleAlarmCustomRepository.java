package com.official.memento.schedule.infrastructure.persistence;

import com.official.memento.member.infrastructure.persistence.entity.QMemberPersonalInfoEntity;
import com.official.memento.schedule.infrastructure.persistence.projection.ScheduleAlarmProjection;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleAlarmCustomRepository {

    private final JPAQueryFactory queryFactory;
    QScheduleEntity schedule = QScheduleEntity.scheduleEntity;
    QMemberPersonalInfoEntity memberPersonalInfo = QMemberPersonalInfoEntity.memberPersonalInfoEntity;

    public List<ScheduleAlarmProjection> findSchedulesWithMemberInfoBetween(
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        List<Tuple> results = queryFactory
                .select(schedule.id, schedule.memberId, schedule.description, schedule.startDate, schedule.endDate, memberPersonalInfo.timeZoneOffset)
                .from(schedule)
                .join(memberPersonalInfo).on(schedule.memberId.eq(memberPersonalInfo.memberId))
                .where(schedule.startDate.between(startDate, endDate))
                .fetch();

        return results.stream()
                .map(tuple -> new ScheduleAlarmProjection(
                        tuple.get(schedule.id),
                        tuple.get(schedule.memberId),
                        tuple.get(schedule.description),
                        tuple.get(schedule.startDate),
                        tuple.get(schedule.endDate),
                        tuple.get(memberPersonalInfo.timeZoneOffset)
                ))
                .collect(Collectors.toList());
    }


}
