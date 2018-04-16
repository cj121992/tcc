package com.melot.common.transaction.tcc.service.handler;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.melot.common.transaction.annotation.TccTransactional;
import com.melot.common.transaction.api.TccStatus;
import com.melot.common.transaction.api.TccTransaction;
import com.melot.common.transaction.api.TccTransactionContext;
import com.melot.common.transaction.tcc.TccTransactionManager;
import com.melot.common.transaction.utils.MethodAnnotationUtil;

@Component
public class StarterTccTransactionHandler implements TccTransactionHandler{

	@Autowired
	private TccTransactionManager tccTransManger;
	
	@Override
	public Object handler(ProceedingJoinPoint pjp,
			TccTransactionContext tccTransactionContext) throws Throwable {
		Object retVal = null;
	
		Method method = MethodAnnotationUtil.getAnnotationedMethod(pjp);
		//根据注解获取指定的confirm方法名称和cancel方法名称
		TccTransactional tcc = method.getAnnotation(TccTransactional.class);
		
		try
		{
			final TccTransaction tccTrans = this.tccTransManger.begin(pjp);
			try{
				retVal = pjp.proceed();
				tccTransManger.updateStatus(tccTrans.getTransactionId(),
                        TccStatus.TRYING.getStatus());

			}
			catch(Throwable t)
			{
				tccTransManger.cancel(tcc.asyncCancel());
				throw t;
			}
			tccTransManger.confirm(tcc.asyncConfirm());
		}
		finally{
			tccTransManger.remove();
		}
		
		return retVal;
	}

}
