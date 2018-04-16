package com.melot.common.transaction.tcc.service;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.melot.common.transaction.annotation.TccTransactional;
import com.melot.common.transaction.api.TccTransactionContext;
import com.melot.common.transaction.tcc.service.handler.TccTransactionHandler;
import com.melot.common.transaction.tcc.service.handler.TccTransactionHandlerManager;
import com.melot.common.transaction.utils.MethodAnnotationUtil;

@Component
public class TccTransactionAspectServiceImpl implements TccTransactionAspectService{

	@Autowired
	private TccTransactionHandlerManager transHandlerManager;
	
	
	@Override
	public Object invoke(TccTransactionContext tccTransactionContext,
			ProceedingJoinPoint point) throws Throwable {
		
		Method method = MethodAnnotationUtil.getAnnotationedMethod(point);
		//根据注解获取指定的propagation
		TccTransactional tcc = method.getAnnotation(TccTransactional.class);
		
		final TccTransactionHandler txTransactionHandler = 
				transHandlerManager.getTccTransactionHandlerByTccContext(tcc.propagation(), tccTransactionContext);
		return txTransactionHandler.handler(point, tccTransactionContext);
	}

}
