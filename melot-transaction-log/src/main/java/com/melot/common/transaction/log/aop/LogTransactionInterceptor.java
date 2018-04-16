package com.melot.common.transaction.log.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public interface LogTransactionInterceptor {
	/**
     *
     * @param pjp 切入点
     * @return Object
     * @throws Throwable 异常
     */
    Object interceptor(ProceedingJoinPoint pjp) throws Throwable;
}
