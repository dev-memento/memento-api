package com.official.memento.global.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 락의 이름 (SpEL 표현식 지원)
     * 예: "'order-reorder:' + #memberId + ':' + #date"
     */
    String key();

    /**
     * 락 획득 대기 시간 (기본 5초)
     */
    long waitTime() default 5L;

    /**
     * 락 유지 시간 (기본 10초, Redisson WatchDog이 자동 연장)
     */
    long leaseTime() default 10L;

    /**
     * 시간 단위 (기본 초)
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
