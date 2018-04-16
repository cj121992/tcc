package com.melot.common.transaction.log.aop.impl;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.melot.common.transaction.log.aop.AbstractLogTransactionAspect;

@Aspect
@Component
public class LogTransactionAspectImpl extends AbstractLogTransactionAspect {

	@Autowired
	public LogTransactionAspectImpl(LogTransactionInterceptorImpl logTransactionInterceptor)
	{
		super.setLogTransactionInterceptor(logTransactionInterceptor);
	}
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
	
}
