package com.melot.common.transaction.kkrpc.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.melot.common.transaction.tcc.aop.AbstractTccTransactionAspect;


@Aspect
@Component
public class KkrpcTccTransactionAspect extends AbstractTccTransactionAspect implements Ordered{

	@Autowired
	public KkrpcTccTransactionAspect(KkrpcTccTransactionInterceptor kkrpcTccTransactionInterceptor)
	{
		super.setTccTransactionInterceptor(kkrpcTccTransactionInterceptor);
	}
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
