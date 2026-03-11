package com.example.monos.common.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Controller（画面単位）ログ
     */
    @Around("execution(* com.example.monos.controller..*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        // 画面単位ログ
        logger.info("【画面処理開始】 {}", method);

        Object result = joinPoint.proceed();

        logger.info("【画面処理終了】 {}", method);
        return result;
    }

    /**
     * Service（メソッド単位）ログ
     */
    @Around("execution(* com.example.monos.service..*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        if (logger.isDebugEnabled()) {
            logger.debug("メソッド開始: {}", method);
        }

        Object result = joinPoint.proceed();

        if (logger.isDebugEnabled()) {
            logger.debug("メソッド終了: {}", method);
        }
        return result;
    }

    /**
     * Mapper（SQL）ログ
     */
    @Around("execution(* com.example.monos.mapper..*(..))")
    public Object logMapper(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        if (logger.isDebugEnabled()) {
            logger.debug("SQL Mapper呼び出し: {}", method);
        }

        Object result = joinPoint.proceed();

        if (logger.isDebugEnabled()) {
            logger.debug("SQL Mapper終了: {}", method);
        }
        return result;
    }
}
