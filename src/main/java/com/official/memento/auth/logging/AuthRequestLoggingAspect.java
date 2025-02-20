package com.official.memento.auth.logging;

import com.official.memento.auth.controller.dto.AuthApiRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthRequestLoggingAspect {

    @Before("execution(* com.official.memento.auth.controller.AuthApiController.login(..))")
    public void logAuthRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof AuthApiRequest) { // 요청 객체인 경우
                log.info("로그인 요청 데이터: {}", arg);
                break; // 요청 객체 하나만 로깅 후 종료
            }
        }
    }
}
