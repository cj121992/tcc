package com.melot.common.transaction.kkrpc.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.melot.common.transaction.api.TccTransactionContext;
import com.melot.common.transaction.api.TccTransactionContextLocal;
import com.melot.common.transaction.tcc.aop.TccTransactionInterceptor;
import com.melot.common.transaction.tcc.service.TccTransactionAspectService;

@Component
public class KkrpcTccTransactionInterceptor implements TccTransactionInterceptor{

	@Autowired
	private TccTransactionAspectService tccTransactionAspectService;
	
	@Override
	public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
		TccTransactionContext tccTransactionContext = TccTransactionContextLocal.getInstance().get();
		return tccTransactionAspectService.invoke(tccTransactionContext, pjp);
	}

}
