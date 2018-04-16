package com.melot.common.transaction.tcc.service.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import com.melot.common.transaction.api.TccTransactionContext;

@Component
public class NormalTccTransactionHandler implements TccTransactionHandler{

	@Override
	public Object handler(ProceedingJoinPoint pjp,
			TccTransactionContext tccTransactionContext) throws Throwable {
		return pjp.proceed();
	}

}
