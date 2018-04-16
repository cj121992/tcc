package com.melot.common.transaction.tcc.service;

import org.aspectj.lang.ProceedingJoinPoint;

import com.melot.common.transaction.api.TccTransactionContext;

public interface TccTransactionAspectService {
	/**
     * tcc 事务切面服务
     *
     * @param tccTransactionContext tcc事务上下文对象
     * @param point                 切点
     * @return object
     * @throws Throwable 异常信息
     */
    Object invoke(TccTransactionContext tccTransactionContext, ProceedingJoinPoint point) throws Throwable;
}
