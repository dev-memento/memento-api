package com.official.memento.global.lock;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * PostgreSQL Advisory Lock을 활용한 애플리케이션 레벨 락
 * - 트랜잭션 범위 내에서 동작 (트랜잭션 종료 시 자동 해제)
 * - 다양한 키 조합을 지원하여 확장성 있는 락 관리 가능
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdvisoryLock {

    private final EntityManager entityManager;

    /**
     * Advisory Lock 획득 (대기 방식)
     * 다른 세션이 락을 보유하고 있으면 해제될 때까지 대기
     *
     * @param lockKey 락 키 (고유한 long 값)
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void lock(final long lockKey) {
        Query query = entityManager.createNativeQuery("SELECT pg_advisory_xact_lock(:lockKey)");
        query.setParameter("lockKey", lockKey);
        query.getSingleResult();
        log.debug("Advisory Lock 획득: key={}", lockKey);
    }

    /**
     * Advisory Lock 획득 시도 (비대기 방식)
     * 락을 즉시 획득할 수 없으면 false 반환
     *
     * @param lockKey 락 키 (고유한 long 값)
     * @return 락 획득 성공 여부
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean tryLock(final long lockKey) {
        Query query = entityManager.createNativeQuery("SELECT pg_try_advisory_xact_lock(:lockKey)");
        query.setParameter("lockKey", lockKey);
        Boolean acquired = (Boolean) query.getSingleResult();

        if (acquired) {
            log.debug("Advisory Lock 획득 성공: key={}", lockKey);
        } else {
            log.debug("Advisory Lock 획득 실패: key={}", lockKey);
        }
        return acquired;
    }
}
